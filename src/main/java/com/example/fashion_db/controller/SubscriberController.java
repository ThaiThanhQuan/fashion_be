package com.example.fashion_db.controller;

import com.example.fashion_db.dto.response.ApiResponse;
import com.example.fashion_db.service.SubscriberService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/subscribers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SubscriberController {

    SubscriberService subscriberService;

    @PostMapping
    public ApiResponse<Void> subscribe(@RequestParam String email) throws MessagingException, UnsupportedEncodingException {
        subscriberService.subscribe(email);
        return ApiResponse.<Void>builder()
                .message("Đăng ký nhận tin thành công!")
                .build();
    }
}
