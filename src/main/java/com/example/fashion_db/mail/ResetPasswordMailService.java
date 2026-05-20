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
public class ResetPasswordMailService {

    JavaMailSender mailSender;

    @NonFinal
    @Value("${spring.mail.username}")
    String from;

    public void send(String to, String username, String token)
            throws MessagingException, UnsupportedEncodingException {

        String resetLink = "http://localhost:3000/reset-password?token=" + token;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from, "COUTURE");
        helper.setTo(to);
        helper.setSubject("Khôi phục mật khẩu COUTURE");

        String html = """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto;
                        padding: 24px; border: 1px solid #e5e7eb; border-radius: 12px;
                        background-color: #ffffff;">
                <h2 style="color: #111827;">Khôi phục mật khẩu COUTURE</h2>
                <p>Chào <b>%s</b>,</p>
                <p>Bạn đã yêu cầu khôi phục mật khẩu tại <b>COUTURE</b>.</p>
                <p>Vui lòng nhấn vào nút bên dưới để đặt lại mật khẩu:</p>
                <div style="margin: 32px 0;">
                    <a href="%s" style="background-color: #111827; color: white; padding: 14px 24px;
                        text-decoration: none; border-radius: 8px; display: inline-block; font-weight: bold;">
                        Đặt lại mật khẩu
                    </a>
                </div>
                <p style="color: #6b7280;">Link này sẽ hết hạn sau 15 phút.</p>
                <p style="color: #6b7280;">Nếu bạn không yêu cầu điều này, vui lòng bỏ qua email này.</p>
            </div>
            """.formatted(username, resetLink);

        helper.setText(html, true);
        mailSender.send(message);
    }
}