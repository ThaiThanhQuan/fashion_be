package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.PricingRequest;
import com.example.fashion_db.dto.response.PricingResponse;
import com.example.fashion_db.entity.Pricing;
import com.example.fashion_db.exception.AppException;
import com.example.fashion_db.exception.ErrorCode;
import com.example.fashion_db.mapper.PricingMapper;
import com.example.fashion_db.repository.PricingRepository;
import com.example.fashion_db.repository.ServiceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PricingService {

    PricingRepository pricingRepository;
    ServiceRepository serviceRepository;
    PricingMapper pricingMapper;

    public PricingResponse createPricing(PricingRequest request) {
        Pricing pricing = pricingMapper.toPricing(request);
        pricing.setService(serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND)));

        return pricingMapper.toPricingResponse(pricingRepository.save(pricing));
    }

    public List<PricingResponse> getPricingsByService(String serviceId) {
        return pricingRepository.findByService_Id(serviceId)
                .stream()
                .map(pricingMapper::toPricingResponse)
                .toList();
    }

    public PricingResponse getPricingById(String pricingId) {
        return pricingMapper.toPricingResponse(
                pricingRepository.findById(pricingId)
                        .orElseThrow(() -> new AppException(ErrorCode.PRICING_NOT_FOUND)));
    }

    public PricingResponse updatePricing(String pricingId, PricingRequest request) {
        Pricing pricing = pricingRepository.findById(pricingId)
                .orElseThrow(() -> new AppException(ErrorCode.PRICING_NOT_FOUND));

        pricingMapper.updatePricing(pricing, request);
        return pricingMapper.toPricingResponse(pricingRepository.save(pricing));
    }

    public void deletePricing(String pricingId) {
        pricingRepository.deleteById(pricingId);
    }
}