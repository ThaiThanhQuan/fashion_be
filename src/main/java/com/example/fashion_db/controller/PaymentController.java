package com.example.fashion_db.controller;

import com.example.fashion_db.entity.Order;
import com.example.fashion_db.enums.OrderStatus;
import com.example.fashion_db.enums.PaymentStatus;
import com.example.fashion_db.mail.MailService;

import com.example.fashion_db.repository.OrderRepository;
import com.example.fashion_db.service.VNPayService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentController {

    OrderRepository orderRepository;
    VNPayService vnPayService;
    MailService mailService;

    // VNPay callback
    @GetMapping("/vnpay-return")
    public void vnpayReturn(
            @RequestParam Map<String, String> params,
            HttpServletResponse response) throws IOException {

        boolean valid = vnPayService.validateCallback(new HashMap<>(params));
        String orderId = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");

        Order order = orderRepository.findById(orderId).orElse(null);

        if (order != null && valid && "00".equals(responseCode)) {
            order.setPaymentStatus(PaymentStatus.PAID);
            order.setStatus(OrderStatus.CONFIRMED);
            orderRepository.save(order);

            // Gửi mail sau khi thanh toán thành công
            mailService.sendOrderConfirmEmail(order);

            response.sendRedirect("http://localhost:3000/order/payment-result?status=success&orderId=" + orderId);
        } else {
            if (order != null) {
                order.setPaymentStatus(PaymentStatus.FAILED);
                orderRepository.save(order);
            }
            response.sendRedirect("http://localhost:3000/order/payment-result?status=failed");
        }
    }
}
