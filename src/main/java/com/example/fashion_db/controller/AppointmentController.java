package com.example.fashion_db.controller;

import com.example.fashion_db.dto.request.AppointmentRequest;
import com.example.fashion_db.dto.response.ApiResponse;
import com.example.fashion_db.dto.response.AppointmentResponse;
import com.example.fashion_db.service.AppointmentService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AppointmentController {

    AppointmentService appointmentService;

    @PostMapping
    public ApiResponse<AppointmentResponse> createAppointment(
            @RequestBody AppointmentRequest request) throws MessagingException, UnsupportedEncodingException {
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.createAppointment(request))
                .build();
    }
}
