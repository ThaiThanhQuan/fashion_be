package com.example.fashion_db.mail;

import com.example.fashion_db.entity.Order;
import com.example.fashion_db.entity.OrderItem;
import com.example.fashion_db.entity.Product;
import com.example.fashion_db.entity.ProductImage;
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
}
