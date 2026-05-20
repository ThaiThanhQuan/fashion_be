package com.example.fashion_db.mail;

import com.example.fashion_db.entity.Order;
import com.example.fashion_db.entity.OrderItem;
import com.example.fashion_db.entity.Product;
import com.example.fashion_db.entity.ProductImage;
import com.example.fashion_db.repository.ProductImageRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
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

    public void send(Order order)
            throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from, "COUTURE");
        helper.setTo(order.getUser().getEmail());
        helper.setSubject("Xac nhan don hang #" + shortOrderId(order.getId()) + " - COUTURE");

        StringBuilder itemRows = new StringBuilder();
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            String imageUrl = productImageRepository
                    .findByProduct_IdAndThumbnailTrue(product.getId())
                    .map(ProductImage::getImagePath)
                    .orElse("");

            itemRows.append("""
                <tr>
                    <td style="padding: 18px 0; border-bottom: 1px solid #eee;">
                        <table style="width: 100%%; border-collapse: collapse;"><tr>
                            <td style="width: 95px; vertical-align: top;">
                                <img src="%s" alt="%s"
                                     style="width: 80px; height: 100px; object-fit: cover;
                                            border-radius: 10px; display: block; border: 1px solid #eee;" />
                            </td>
                            <td style="padding-left: 16px; vertical-align: top;">
                                <div style="font-weight: 700; color: #111; margin-bottom: 6px; font-size: 15px;">%s</div>
                                <div style="font-size: 13px; color: #666; margin-bottom: 4px;">Size: %s</div>
                                <div style="font-size: 13px; color: #666;">So luong: %d</div>
                            </td>
                            <td style="text-align: right; vertical-align: top; font-weight: 700;
                                       white-space: nowrap; font-size: 15px; color: #111;">%s</td>
                        </tr></table>
                    </td>
                </tr>
                """.formatted(
                    imageUrl,
                    escapeHtml(product.getTitle()),
                    escapeHtml(product.getTitle()),
                    escapeHtml(item.getSize()),
                    item.getQuantity(),
                    formatCurrency(item.getPrice() * item.getQuantity())
            ));
        }

        String createdAt = order.getCreatedAt() == null ? ""
                : order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        String content = """
            <div style="font-family: Arial, sans-serif; max-width: 650px; margin: auto;
                        padding: 28px; border: 1px solid #eee; color: #222; background: #fff;">
                <div style="margin-bottom: 28px;">
                    <h1 style="margin: 0 0 8px; letter-spacing: 2px; color: #111;">COUTURE</h1>
                    <p style="margin: 0; color: #666;">Cam on ban da dat hang. Don hang cua ban da duoc ghi nhan.</p>
                </div>
                <div style="background: #f7f5f2; padding: 16px; margin-bottom: 24px;
                            border-radius: 10px; line-height: 1.8;">
                    <div><b>Ma don:</b> #%s</div>
                    <div><b>Ngay dat:</b> %s</div>
                    <div><b>Thanh toan:</b> %s</div>
                    <div><b>Trang thai:</b> %s</div>
                </div>
                <h2 style="font-size: 18px; margin: 0 0 12px; color: #111;">Thong tin giao hang</h2>
                <div style="margin-bottom: 28px; line-height: 1.7;">
                    <div><b>%s</b></div>
                    <div>%s</div>
                    <div>%s</div>
                </div>
                <h2 style="font-size: 18px; margin: 0 0 12px; color: #111;">San pham</h2>
                <table style="width: 100%%; border-collapse: collapse; margin-bottom: 28px;">%s</table>
                <div style="border-top: 1px solid #ddd; padding-top: 18px;">
                    <div style="display: flex; justify-content: space-between; margin: 6px 0;">
                        <span>Tam tinh</span><b>%s</b>
                    </div>
                    <div style="display: flex; justify-content: space-between; margin: 6px 0;">
                        <span>Phi van chuyen</span><b>%s</b>
                    </div>
                    <div style="display: flex; justify-content: space-between; margin: 6px 0;">
                        <span>Thue</span><b>%s</b>
                    </div>
                    <div style="display: flex; justify-content: space-between; margin-top: 16px;
                                font-size: 20px; font-weight: 700; color: #111;">
                        <span>Tong cong</span><span>%s</span>
                    </div>
                </div>
                <p style="margin-top: 28px; color: #666; font-size: 13px; line-height: 1.7;">
                    COUTURE se lien he voi ban khi don hang duoc xu ly.
                </p>
            </div>
            """.formatted(
                shortOrderId(order.getId()), createdAt,
                order.getPaymentMethod(), order.getStatus(),
                escapeHtml(order.getAddress().getRecipientName()),
                escapeHtml(order.getAddress().getPhone()),
                escapeHtml(order.getAddress().getAddress()),
                itemRows,
                formatCurrency(order.getSubtotal()),
                formatCurrency(order.getShippingFee()),
                formatCurrency(order.getTax()),
                formatCurrency(order.getGrandTotal())
        );

        helper.setText(content, true);
        mailSender.send(message);
    }

    private String shortOrderId(String orderId) {
        if (orderId == null || orderId.length() <= 8) return orderId;
        return orderId.substring(0, 8).toUpperCase();
    }

    private String formatCurrency(long amount) {
        return NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN")).format(amount);
    }

    private String escapeHtml(String value) {
        if (value == null) return "";
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
