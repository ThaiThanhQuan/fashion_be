package com.example.fashion_db.mail;

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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscribeMailService {

    JavaMailSender mailSender;

    @NonFinal
    @Value("${spring.mail.username}")
    String from;

    public void send(String to)
            throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from, "COUTURE");
        helper.setTo(to);
        helper.setSubject("Đăng ký nhận tin thành công!");

        String content = """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto;
                        padding: 30px; background-color: #fdfaf6; border-radius: 12px;
                        border: 1px solid #eee; color: #333;">
                <h1 style="color: #785a1a; text-align: center; margin-bottom: 10px;">COUTURE</h1>
                <p style="font-size: 16px;">Xin chào,</p>
                <p style="font-size: 16px; line-height: 1.7;">
                    Cảm ơn bạn đã đăng ký nhận tin từ <strong>COUTURE</strong>.
                </p>
                <p style="font-size: 16px; line-height: 1.7;">Bạn sẽ nhận được thông báo mới nhất về:</p>
                <ul style="line-height: 1.8; font-size: 14px;">
                    <li>Bộ sưu tập mới</li>
                </ul>
                <div style="margin-top: 30px; padding-top: 20px; border-top: 1px solid #ddd;
                            text-align: center; font-size: 14px; color: #888;">
                    © 2026 COUTURE. All rights reserved.
                </div>
            </div>
            """;

        helper.setText(content, true);
        mailSender.send(message);
    }
}
