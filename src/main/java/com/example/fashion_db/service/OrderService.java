package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.OrderItemRequest;
import com.example.fashion_db.dto.request.OrderRequest;
import com.example.fashion_db.dto.response.OrderResponse;
import com.example.fashion_db.dto.response.PageResponse;
import com.example.fashion_db.entity.*;
import com.example.fashion_db.enums.OrderStatus;
import com.example.fashion_db.enums.PaymentMethod;
import com.example.fashion_db.enums.PaymentStatus;
import com.example.fashion_db.exception.AppException;
import com.example.fashion_db.exception.ErrorCode;
import com.example.fashion_db.mail.MailService;
import com.example.fashion_db.mapper.OrderItemMapper;
import com.example.fashion_db.mapper.OrderMapper;
import com.example.fashion_db.repository.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderService {

    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;
    ProductRepository productRepository;
    ProductVariantsRepository productVariantsRepository;
    AddressRepository addressRepository;
    UserRepository userRepository;
    OrderMapper orderMapper;
    ProductImageRepository productImageRepository;
    MailService mailService;
    VNPayService vnPayService;

    private String getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((Jwt) authentication.getPrincipal()).getClaim("userId");
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        String userId = getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // 1. Tính tiền
        List<OrderItem> orderItems = new ArrayList<>();
        long subtotal = 0;

        for (OrderItemRequest itemRequest : request.getOrderItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

            // Kiểm tra stock
            ProductVariants variant = productVariantsRepository
                    .findByProduct_IdAndSize(product.getId(), itemRequest.getSize())
                    .orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_FOUND));

            if (variant.getStock() < itemRequest.getQuantity())
                throw new AppException(ErrorCode.OUT_OF_STOCK);

            // Trừ stock
            variant.setStock(variant.getStock() - itemRequest.getQuantity());
            productVariantsRepository.save(variant);

            // Tạo order item
            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .size(itemRequest.getSize())
                    .quantity(itemRequest.getQuantity())
                    .price(product.getPrice())
                    .build();

            orderItems.add(orderItem);
            subtotal += product.getPrice() * itemRequest.getQuantity();
        }

        // 2. Tính phí
        long shippingFee = 30000L;
        long tax = (long) (subtotal * 0.1);
        long grandTotal = subtotal + shippingFee + tax;

        // 3. Tạo order
        Order order = Order.builder()
                .user(user)
                .address(addressRepository.findByIdAndUser(request.getAddressId(), user)
                        .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED)))
                .subtotal(subtotal)
                .shippingFee(shippingFee)
                .tax(tax)
                .grandTotal(grandTotal)
                .status(OrderStatus.PENDING)
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        Order savedOrder = orderRepository.save(order);
        orderItems.forEach(item -> {
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        });

        Order finalOrder = orderRepository.findById(savedOrder.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // COD → gửi mail xác nhận
        if (request.getPaymentMethod() == PaymentMethod.COD) {
            mailService.sendOrderConfirmEmail(finalOrder);
        }

        // BANK_TRANSFER → tạo VNPay URL
        if (request.getPaymentMethod() == PaymentMethod.BANK_TRANSFER) {
            String paymentUrl = vnPayService.createPaymentUrl(
                    savedOrder.getId(),
                    savedOrder.getGrandTotal()
            );
            finalOrder.setPaymentUrl(paymentUrl);
            orderRepository.save(finalOrder);
        }

        return orderMapper.toOrderResponse(finalOrder);
    }

    public PageResponse<OrderResponse> getMyOrders(int page, int size) {
        String userId = getCurrentUserId();
        return PageResponse.of(orderRepository.findByUser_Id(userId, PageRequest.of(page, size))
                .map(this::buildOrderResponse));
    }

    public OrderResponse getOrderById(String orderId) {
        return buildOrderResponse(
                orderRepository.findById(orderId)
                        .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND)));
    }

    // Admin: lấy tất cả orders
    public PageResponse<OrderResponse> getAllOrders(int page, int size) {
        return PageResponse.of(orderRepository.findAll(PageRequest.of(page, size))
                .map(this::buildOrderResponse));
    }

    // Admin: cập nhật status
    public OrderResponse updateOrderStatus(String orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        order.setStatus(status);
        return buildOrderResponse(orderRepository.save(order));
    }

    // User: hủy đơn
    public OrderResponse cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getStatus() != OrderStatus.PENDING)
            throw new AppException(ErrorCode.CANNOT_CANCEL_ORDER);

        // Hoàn stock
        order.getOrderItems().forEach(item -> {
            productVariantsRepository.findByProduct_IdAndSize(
                            item.getProduct().getId(), item.getSize())
                    .ifPresent(variant -> {
                        variant.setStock(variant.getStock() + item.getQuantity());
                        productVariantsRepository.save(variant);
                    });
        });

        order.setStatus(OrderStatus.CANCELLED);
        return buildOrderResponse(orderRepository.save(order));
    }

    private OrderResponse buildOrderResponse(Order order) {
        OrderResponse response = orderMapper.toOrderResponse(order);
        response.getOrderItems().forEach(item -> {
            String thumbnail = productImageRepository
                    .findByProduct_IdAndThumbnailTrue(item.getProduct().getId())
                    .map(ProductImage::getImagePath)
                    .orElse(null);
            item.getProduct().setThumbnail(thumbnail);
        });
        return response;
    }
}
