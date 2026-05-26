package com.example.fashion_db.mail;

import com.example.fashion_db.dto.response.AppointmentResponse;
import com.example.fashion_db.entity.Order;
import com.example.fashion_db.enums.OrderStatus;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailService {

    ResetPasswordMailService resetPasswordMailService;
    SubscribeMailService subscribeMailService;
    CollectionMailService collectionMailService;
    AppointmentMailService appointmentMailService;
    OrderMailService orderMailService;

    public void sendResetPasswordEmail(String to, String username, String token)
            throws MessagingException, UnsupportedEncodingException {
        resetPasswordMailService.send(to, username, token);
    }

    public void sendSubscribeConfirmEmail(String to)
            throws MessagingException, UnsupportedEncodingException {
        subscribeMailService.send(to);
    }

    public void sendNewCollectionEmail(String to, String title, String slug, String thumbnail)
            throws MessagingException, UnsupportedEncodingException {
        collectionMailService.send(to, title, slug, thumbnail);
    }

    public void sendAppointmentConfirmEmail(AppointmentResponse appointment)
            throws MessagingException, UnsupportedEncodingException {
        appointmentMailService.send(appointment);
    }

    public void sendOrderConfirmEmail(Order order) {
        orderMailService.sendOrderConfirmEmail(order);
    }

    public void sendOrderStatusUpdateEmail(Order order, OrderStatus newStatus) {
        orderMailService.sendOrderStatusUpdateEmail(order, newStatus);
    }
}
