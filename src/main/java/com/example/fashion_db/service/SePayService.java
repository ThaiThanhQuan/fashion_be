package com.example.fashion_db.service;

import com.example.fashion_db.enums.OrderStatus;
import com.example.fashion_db.enums.PaymentStatus;
import com.example.fashion_db.mail.MailService;
import com.example.fashion_db.repository.OrderRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class SePayService {

    @Value("${sepay.bank-account}")
    String bankAccount;

    @Value("${sepay.bank-name}")
    String bankName;

    @Value("${sepay.account-name}")
    String accountName;

    OrderRepository orderRepository;
    MailService mailService;

//    @Value("${sepay.webhook-secret}")
//    String webhookSecret;

    // Tạo nội dung chuyển khoản
    public String generateTransferContent(String orderId) {
        return "COUTURE " + orderId.substring(0, 8).toUpperCase();
    }

    // Tạo QR code URL dùng SePay API
    public String generateQRCode(String orderId, Long amount) {
        String content = generateTransferContent(orderId);
        // SePay QR format
        return String.format(
                "https://qr.sepay.vn/img?bank=%s&acc=%s&template=compact&amount=%d&des=%s",
                bankName, bankAccount, amount, content
        );
    }

    public boolean handleWebhook(Map<String, Object> payload) {
        try {
            String transferContent = (String) payload.get("description");
            Long amount = Long.parseLong(payload.get("transferAmount").toString());
            String type = (String) payload.get("transferType");

            if (!"in".equals(type)) return true;

            orderRepository.findByTransferContent(transferContent)
                    .ifPresent(order -> {
                        if (order.getGrandTotal().equals(amount)
                                && order.getPaymentStatus() == PaymentStatus.PENDING) {

                            order.setPaymentStatus(PaymentStatus.PAID);
                            order.setStatus(OrderStatus.CONFIRMED);
                            orderRepository.save(order);

                            mailService.sendOrderConfirmEmail(order);
                            log.info("Order {} confirmed via SePay", order.getId());
                        }
                    });

            return true;

        } catch (Exception e) {
            log.error("SePay webhook error: {}", e.getMessage());
            return false;
        }
    }

    // Validate webhook signature
//    public boolean validateWebhook(String signature, String body) {
//        try {
//            Mac hmac = Mac.getInstance("HmacSHA256");
//            SecretKeySpec secretKey = new SecretKeySpec(
//                    webhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
//            hmac.init(secretKey);
//            byte[] hash = hmac.doFinal(body.getBytes(StandardCharsets.UTF_8));
//            StringBuilder result = new StringBuilder();
//            for (byte b : hash) result.append(String.format("%02x", b));
//            return result.toString().equals(signature);
//        } catch (Exception e) {
//            return false;
//        }
//    }
}
