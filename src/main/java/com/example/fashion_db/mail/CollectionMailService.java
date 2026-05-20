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
public class CollectionMailService {

    JavaMailSender mailSender;

    @NonFinal
    @Value("${spring.mail.username}")
    String from;

    public void send(String to, String collectionTitle, String collectionSlug, String thumbnail)
            throws MessagingException, UnsupportedEncodingException {

        String collectionUrl = "http://localhost:3000/collection/" + collectionSlug;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from, "COUTURE");
        helper.setTo(to);
        helper.setSubject("Bộ sưu tập mới: " + collectionTitle);

        String content = """
            <div style="font-family: Arial, sans-serif; max-width: 650px; margin: auto;
                        background: #fdfaf6; border-radius: 16px; overflow: hidden; border: 1px solid #eee;">
                <img src="%s" alt="%s"
                     style="width: 100%%; height: 350px; object-fit: cover; display: block;" />
                <div style="padding: 32px;">
                    <h1 style="margin: 0 0 10px; color: #785a1a; font-size: 32px;">COUTURE</h1>
                    <p style="font-size: 16px; color: #444; line-height: 1.8;">Bộ sưu tập mới vừa ra mắt:</p>
                    <h2 style="margin-top: 10px; margin-bottom: 20px; font-size: 28px; color: #111;">%s</h2>
                    <p style="font-size: 15px; color: #666; line-height: 1.8;">
                        Khám phá những thiết kế mới nhất và trải nghiệm phong cách thời trang đẳng cấp từ COUTURE.
                    </p>
                    <div style="margin-top: 35px;">
                        <a href="%s" style="background: #785a1a; color: white; text-decoration: none;
                            padding: 14px 28px; border-radius: 10px; font-weight: bold; display: inline-block;">
                            Khám phá ngay
                        </a>
                    </div>
                    <div style="margin-top: 40px; padding-top: 20px; border-top: 1px solid #ddd;
                                font-size: 13px; color: #888; text-align: center;">
                        © 2026 COUTURE. All rights reserved.
                    </div>
                </div>
            </div>
            """.formatted(thumbnail, collectionTitle, collectionTitle, collectionUrl);

        helper.setText(content, true);
        mailSender.send(message);
    }
}