package com.example.fashion_db.entity;

import com.example.fashion_db.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String customerName;
    String customerEmail;
    String customerPhone;
    String specialRequest;
    LocalDate appointmentDate;
    String appointmentTime;

    @Enumerated(EnumType.STRING)
    AppointmentStatus status;

    @CreationTimestamp
    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "service_id")
    Service service;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    Artist artist;
}
