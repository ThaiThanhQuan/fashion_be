package com.example.fashion_db.controller;

import com.example.fashion_db.dto.request.ServiceRequest;
import com.example.fashion_db.dto.response.ApiResponse;
import com.example.fashion_db.dto.response.PageResponse;
import com.example.fashion_db.dto.response.ServiceResponse;
import com.example.fashion_db.service.ServiceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ServiceController {

    ServiceService serviceService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ServiceResponse> createService(@ModelAttribute ServiceRequest request) {
        return ApiResponse.<ServiceResponse>builder()
                .result(serviceService.createService(request))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<ServiceResponse>> getAllServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ServiceResponse>>builder()
                .result(serviceService.getAllServices(page, size))
                .build();
    }

    @GetMapping("/{serviceId}")
    public ApiResponse<ServiceResponse> getServiceById(@PathVariable String serviceId) {
        return ApiResponse.<ServiceResponse>builder()
                .result(serviceService.getServiceById(serviceId))
                .build();
    }

    @GetMapping("/slug/{slug}")
    public ApiResponse<ServiceResponse> getServiceBySlug(@PathVariable String slug) {
        return ApiResponse.<ServiceResponse>builder()
                .result(serviceService.getServiceBySlug(slug))
                .build();
    }

    @PutMapping(value = "/{serviceId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ServiceResponse> updateService(
            @PathVariable String serviceId,
            @ModelAttribute ServiceRequest request) {
        return ApiResponse.<ServiceResponse>builder()
                .result(serviceService.updateService(serviceId, request))
                .build();
    }

    @DeleteMapping("/{serviceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteService(@PathVariable String serviceId) {
        serviceService.deleteService(serviceId);
        return ApiResponse.<Void>builder()
                .message("Delete service successfully")
                .build();
    }
}