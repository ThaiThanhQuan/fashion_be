package com.example.fashion_db.controller;

import com.example.fashion_db.entity.Order;
import com.example.fashion_db.enums.OrderStatus;
import com.example.fashion_db.enums.PaymentStatus;
import com.example.fashion_db.mail.MailService;

import com.example.fashion_db.repository.OrderRepository;
import com.example.fashion_db.service.SePayService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SePayWebhookController {

    SePayService sePayService;

    @PostMapping("/sepay-webhook")
    public ResponseEntity<Map<String, String>> handleWebhook(
            @RequestHeader(value = "Secure-Token", required = false) String signature,
            @RequestBody Map<String, Object> payload) {

        log.info("SePay webhook received: {}", payload);
        boolean success = sePayService.handleWebhook(payload);
        return ResponseEntity.ok(Map.of("success", String.valueOf(success)));
    }
}
