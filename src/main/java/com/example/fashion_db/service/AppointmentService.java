package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.AppointmentRequest;
import com.example.fashion_db.dto.response.AppointmentResponse;
import com.example.fashion_db.entity.Appointment;
import com.example.fashion_db.entity.Artist;
import com.example.fashion_db.entity.Service;
import com.example.fashion_db.enums.AppointmentStatus;
import com.example.fashion_db.exception.AppException;
import com.example.fashion_db.exception.ErrorCode;
import com.example.fashion_db.repository.AppointmentRepository;
import com.example.fashion_db.repository.ArtistRepository;
import com.example.fashion_db.repository.ServiceRepository;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AppointmentService {

    AppointmentRepository appointmentRepository;
    ServiceRepository serviceRepository;
    ArtistRepository artistRepository;
    MailService mailService;

    public AppointmentResponse createAppointment(AppointmentRequest request) throws MessagingException, UnsupportedEncodingException {
        Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND));

        Artist artist = artistRepository.findById(request.getArtistId())
                .orElseThrow(() -> new AppException(ErrorCode.ARTIST_NOT_FOUND));

        Appointment appointment = Appointment.builder()
                .customerName(request.getCustomerName())
                .customerEmail(request.getCustomerEmail())
                .customerPhone(request.getCustomerPhone())
                .specialRequest(request.getSpecialRequest())
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .status(AppointmentStatus.PENDING)
                .service(service)
                .artist(artist)
                .build();

        appointmentRepository.save(appointment);

        AppointmentResponse response = AppointmentResponse.builder()
                .id(appointment.getId())
                .customerName(appointment.getCustomerName())
                .customerEmail(appointment.getCustomerEmail())
                .customerPhone(appointment.getCustomerPhone())
                .specialRequest(appointment.getSpecialRequest())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus())
                .createdAt(appointment.getCreatedAt())
                .serviceId(service.getId())
                .serviceTitle(service.getTitle())
                .artistId(artist.getId())
                .artistName(artist.getName())
                .build();

        // Gửi mail xác nhận
        mailService.sendAppointmentConfirmEmail(response);

        return response;
    }
}