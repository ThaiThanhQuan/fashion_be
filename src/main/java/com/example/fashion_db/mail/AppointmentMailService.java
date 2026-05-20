package com.example.fashion_db.mail;

import com.example.fashion_db.dto.response.AppointmentResponse;
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
public class AppointmentMailService {

    JavaMailSender mailSender;

    @NonFinal
    @Value("${spring.mail.username}")
    String from;

    public void send(AppointmentResponse appointment)
            throws MessagingException, UnsupportedEncodingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

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

            Đội ngũ COUTURE sẽ sớm liên hệ với bạn để xác nhận lịch hẹn.

            Trân trọng,
            COUTURE Maison de Mode
            Luxury Tailoring • Artistic Experience • Private Styling
            """.formatted(
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