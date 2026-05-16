package com.example.fashion_db.service;

import com.example.fashion_db.dto.response.AppointmentResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MailService {

    JavaMailSender mailSender;

    @NonFinal
    @Value("${spring.mail.username}")
    String from;

    public void sendResetPasswordEmail(String to, String username, String token) throws MessagingException, UnsupportedEncodingException {

        String resetLink =
                "http://localhost:3000/reset-password?token=" + token;

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from, "COUTURE");
        helper.setTo(to);
        helper.setSubject("Khôi phục mật khẩu COUTURE");

        String html = """
            <div style="
                font-family: Arial, sans-serif;
                max-width: 600px;
                margin: auto;
                padding: 24px;
                border: 1px solid #e5e7eb;
                border-radius: 12px;
                background-color: #ffffff;
            ">
                <h2 style="color: #111827;">
                    Khôi phục mật khẩu COUTURE
                </h2>
        
                <p>Chào <b>%s</b>,</p>
        
                <p>
                    Bạn đã yêu cầu khôi phục mật khẩu tại
                    <b>COUTURE</b>.
                </p>
        
                <p>
                    Vui lòng nhấn vào nút bên dưới để đặt lại mật khẩu:
                </p>
        
                <div style="margin: 32px 0;">
                    <a href="%s"
                       style="
                            background-color: #111827;
                            color: white;
                            padding: 14px 24px;
                            text-decoration: none;
                            border-radius: 8px;
                            display: inline-block;
                            font-weight: bold;
                       ">
                        Đặt lại mật khẩu
                    </a>
                </div>
        
                <p style="color: #6b7280;">
                    Link này sẽ hết hạn sau 15 phút.
                </p>
        
                <p style="color: #6b7280;">
                    Nếu bạn không yêu cầu điều này,
                    vui lòng bỏ qua email này.
                </p>
            </div>
            """.formatted(username, resetLink);

        helper.setText(html, true);

        mailSender.send(message);
    }

    // Gửi mail xác nhận đăng ký
    public void sendSubscribeConfirmEmail(String to) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Email người gửi + tên hiển thị
        helper.setFrom(from, "COUTURE");

        helper.setTo(to);
        helper.setSubject("Đăng ký nhận tin thành công!");

        String content = """
        <div style="
            font-family: Arial, sans-serif;
            max-width: 600px;
            margin: auto;
            padding: 30px;
            background-color: #fdfaf6;
            border-radius: 12px;
            border: 1px solid #eee;
            color: #333;
        ">
            <h1 style="
                color: #785a1a;
                text-align: center;
                margin-bottom: 10px;
            ">
                COUTURE
            </h1>

            <p style="font-size: 16px;">
                Xin chào,
            </p>

            <p style="font-size: 16px; line-height: 1.7;">
                Cảm ơn bạn đã đăng ký nhận tin từ 
                <strong>COUTURE</strong>.
            </p>

            <p style="font-size: 16px; line-height: 1.7;">
                Bạn sẽ nhận được thông báo mới nhất về:
            </p>

            <ul style="line-height: 1.8; font-size: 14px;">
                <li>Bộ sưu tập mới</li>
            </ul>

            <div style="
                margin-top: 30px;
                padding-top: 20px;
                border-top: 1px solid #ddd;
                text-align: center;
                font-size: 14px;
                color: #888;
            ">
                © 2026 COUTURE. All rights reserved.
            </div>
        </div>
        """;

        // true = gửi HTML
        helper.setText(content, true);

        mailSender.send(message);
    }

    // Gửi mail thông báo collection mới
    public void sendNewCollectionEmail(
            String to,
            String collectionTitle,
            String collectionSlug,
            String thumbnail
    ) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from, "COUTURE");

        helper.setTo(to);

        helper.setSubject("Bộ sưu tập mới: " + collectionTitle);

        String collectionUrl =
                "http://localhost:3000/collection/" + collectionSlug;

        String content = """
        <div style="
            font-family: Arial, sans-serif;
            max-width: 650px;
            margin: auto;
            background: #fdfaf6;
            border-radius: 16px;
            overflow: hidden;
            border: 1px solid #eee;
        ">

            <!-- Banner -->
            <img
                src="%s"
                alt="%s"
                style="
                    width: 100%%;
                    height: 350px;
                    object-fit: cover;
                    display: block;
                "
            />

            <div style="padding: 32px;">

                <h1 style="
                    margin: 0 0 10px;
                    color: #785a1a;
                    font-size: 32px;
                ">
                    COUTURE
                </h1>

                <p style="
                    font-size: 16px;
                    color: #444;
                    line-height: 1.8;
                ">
                    Bộ sưu tập mới vừa ra mắt:
                </p>

                <h2 style="
                    margin-top: 10px;
                    margin-bottom: 20px;
                    font-size: 28px;
                    color: #111;
                ">
                    %s
                </h2>

                <p style="
                    font-size: 15px;
                    color: #666;
                    line-height: 1.8;
                ">
                    Khám phá những thiết kế mới nhất và trải nghiệm
                    phong cách thời trang đẳng cấp từ COUTURE.
                </p>

                <div style="margin-top: 35px;">
                    <a
                        href="%s"
                        style="
                            background: #785a1a;
                            color: white;
                            text-decoration: none;
                            padding: 14px 28px;
                            border-radius: 10px;
                            font-weight: bold;
                            display: inline-block;
                        "
                    >
                        Khám phá ngay
                    </a>
                </div>

                <div style="
                    margin-top: 40px;
                    padding-top: 20px;
                    border-top: 1px solid #ddd;
                    font-size: 13px;
                    color: #888;
                    text-align: center;
                ">
                    © 2026 COUTURE. All rights reserved.
                </div>

            </div>
        </div>
        """.formatted(
                thumbnail,
                collectionTitle,
                collectionTitle,
                collectionUrl
        );

        helper.setText(content, true);

        mailSender.send(message);
    }

    public void sendAppointmentConfirmEmail(AppointmentResponse appointment)
            throws MessagingException, UnsupportedEncodingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(from, "COUTURE");
        helper.setTo(appointment.getCustomerEmail());

        helper.setSubject("Xác nhận lịch hẹn • COUTURE Maison de Mode");

        String specialRequest =
                (appointment.getSpecialRequest() != null
                        && !appointment.getSpecialRequest().trim().isEmpty())
                        ? """
                    
                    ━━━━━━━━━━━━━━━━━━━━━━━━━━━
                    YÊU CẦU ĐẶC BIỆT
                    %s
                    """.formatted(appointment.getSpecialRequest())
                        : "";

        helper.setText("""
            Kính gửi %s,

            COUTURE Maison de Mode chân thành cảm ơn bạn đã lựa chọn trải nghiệm dịch vụ của chúng tôi.

            Lịch hẹn của bạn đã được ghi nhận thành công với thông tin chi tiết như sau:

            ━━━━━━━━━━━━━━━━━━━━━━━━━━━
                     THÔNG TIN LỊCH HẸN
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━

            DỊCH VỤ
            %s

            CHUYÊN GIA
            %s

            NGÀY HẸN
            %s

            THỜI GIAN
            %s
            %s

            ━━━━━━━━━━━━━━━━━━━━━━━━━━━

            Đội ngũ COUTURE sẽ sớm liên hệ với bạn để xác nhận lịch hẹn và chuẩn bị trải nghiệm tốt nhất dành riêng cho bạn.

            Chúng tôi rất mong được đồng hành cùng bạn trong hành trình nghệ thuật, phong cách và sự tinh tế.

            Trân trọng,

            COUTURE Maison de Mode
            Luxury Tailoring • Artistic Experience • Private Styling
            """
                .formatted(
                        appointment.getCustomerName(),
                        appointment.getServiceTitle(),
                        appointment.getArtistName(),
                        appointment.getAppointmentDate(),
                        appointment.getAppointmentTime(),
                        specialRequest
                ));

        mailSender.send(mimeMessage);
    }
}
