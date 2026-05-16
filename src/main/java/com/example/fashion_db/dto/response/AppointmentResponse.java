package com.example.fashion_db.dto.response;

import com.example.fashion_db.enums.AppointmentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentResponse {
    String id;
    String customerName;
    String customerEmail;
    String customerPhone;
    String specialRequest;
    LocalDate appointmentDate;
    String appointmentTime;
    AppointmentStatus status;
    LocalDateTime createdAt;
    String serviceId;
    String serviceTitle;
    String artistId;
    String artistName;
}