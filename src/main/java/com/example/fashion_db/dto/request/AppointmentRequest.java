package com.example.fashion_db.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentRequest {
    String customerName;
    String customerEmail;
    String customerPhone;
    String specialRequest;
    LocalDate appointmentDate;
    String appointmentTime;
    String serviceId;
    String artistId;
}