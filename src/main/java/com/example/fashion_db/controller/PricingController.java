package com.example.fashion_db.controller;

import com.example.fashion_db.dto.request.PricingRequest;
import com.example.fashion_db.dto.response.ApiResponse;
import com.example.fashion_db.dto.response.PricingResponse;
import com.example.fashion_db.service.PricingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pricing")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PricingController {

    PricingService pricingService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PricingResponse> createPricing(@RequestBody PricingRequest request) {
        return ApiResponse.<PricingResponse>builder()
                .result(pricingService.createPricing(request))
                .build();
    }

    @GetMapping("/service/{serviceId}")
    public ApiResponse<List<PricingResponse>> getPricingsByService(
            @PathVariable String serviceId) {
        return ApiResponse.<List<PricingResponse>>builder()
                .result(pricingService.getPricingsByService(serviceId))
                .build();
    }

    @GetMapping("/{pricingId}")
    public ApiResponse<PricingResponse> getPricingById(@PathVariable String pricingId) {
        return ApiResponse.<PricingResponse>builder()
                .result(pricingService.getPricingById(pricingId))
                .build();
    }

    @PutMapping("/{pricingId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PricingResponse> updatePricing(
            @PathVariable String pricingId,
            @RequestBody PricingRequest request) {
        return ApiResponse.<PricingResponse>builder()
                .result(pricingService.updatePricing(pricingId, request))
                .build();
    }

    @DeleteMapping("/{pricingId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deletePricing(@PathVariable String pricingId) {
        pricingService.deletePricing(pricingId);
        return ApiResponse.<Void>builder()
                .message("Delete pricing successfully")
                .build();
    }
}