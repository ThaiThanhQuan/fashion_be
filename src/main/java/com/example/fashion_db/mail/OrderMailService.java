package com.example.fashion_db.mail;

import com.example.fashion_db.entity.Order;
import com.example.fashion_db.entity.OrderItem;
import com.example.fashion_db.entity.Product;
import com.example.fashion_db.entity.ProductImage;
import com.example.fashion_db.enums.OrderStatus;
import com.example.fashion_db.enums.PaymentMethod;
import com.example.fashion_db.repository.ProductImageRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderMailService {

    JavaMailSender mailSender;
    ProductImageRepository productImageRepository;

    @NonFinal
    @Value("${spring.mail.username}")
    String from;

    public void sendOrderConfirmEmail(Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(order.getUser().getEmail());
        message.setSubject("Đặt hàng thành công - COUTURE #" + order.getId().substring(0, 8).toUpperCase());
        message.setText(
                "Kính gửi " + order.getUser().getUsername() + ",\n\n" +
                        "Đơn hàng của bạn đã được đặt thành công!\n\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        "Mã đơn hàng : #" + order.getId().substring(0, 8).toUpperCase() + "\n" +
                        "Tổng tiền   : " + String.format("%,.0f", (double) order.getGrandTotal()) + " VND\n" +
                        "Thanh toán  : " + (order.getPaymentMethod() == PaymentMethod.COD ? "Tiền mặt (COD)" : "Chuyển khoản") + "\n" +
                        "Trạng thái  : Chờ xác nhận\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                        "Chúng tôi sẽ liên hệ xác nhận sớm nhất.\n\n" +
                        "Trân trọng,\nCOUTURE MAISON DE MODE"
        );
        mailSender.send(message);
    }

    @Async
    public void sendOrderStatusUpdateEmail(Order order, OrderStatus newStatus) {
        String statusLabel = getStatusLabel(newStatus);
        String orderCode = "#" + order.getId().substring(0, 8).toUpperCase();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(order.getUser().getEmail());
        message.setSubject("Cập nhật đơn hàng " + orderCode + " - " + statusLabel);
        message.setText(
                "Kính gửi " + order.getUser().getUsername() + ",\n\n" +
                        "Đơn hàng " + orderCode + " của bạn đã được cập nhật trạng thái.\n\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        "Mã đơn hàng  : " + orderCode + "\n" +
                        "Tổng tiền    : " + String.format("%,.0f", (double) order.getGrandTotal()) + " VND\n" +
                        "Trạng thái   : " + statusLabel + "\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                        getStatusMessage(newStatus) + "\n\n" +
                        "Trân trọng,\nCOUTURE MAISON DE MODE"
        );
        mailSender.send(message);
    }

    private String getStatusLabel(OrderStatus status) {
        return switch (status) {
            case PENDING -> "Chờ xác nhận";
            case CONFIRMED -> "Đã xác nhận";
            case SHIPPING -> "Đang giao hàng";
            case DELIVERED -> "Đã giao hàng";
            case CANCELLED -> "Đã hủy";
            case REFUNDED -> "Đã hoàn tiền";
        };
    }

    private String getStatusMessage(OrderStatus status) {
        return switch (status) {
            case CONFIRMED -> "Đơn hàng của bạn đã được xác nhận và đang được chuẩn bị.";
            case SHIPPING -> "Đơn hàng của bạn đang được vận chuyển. Vui lòng chú ý điện thoại để nhận hàng.";
            case DELIVERED -> "Đơn hàng đã được giao thành công. Cảm ơn bạn đã mua sắm tại COUTURE!";
            case CANCELLED -> "Đơn hàng của bạn đã bị hủy. Nếu bạn có thắc mắc, vui lòng liên hệ chúng tôi.";
            case REFUNDED -> "Đơn hàng đã được hoàn tiền. Vui lòng kiểm tra tài khoản của bạn.";
            default -> "Trạng thái đơn hàng đã được cập nhật.";
        };
    }
}
