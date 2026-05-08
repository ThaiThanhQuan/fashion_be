package com.example.fashion_db.mapper;

import com.example.fashion_db.dto.request.PricingRequest;
import com.example.fashion_db.dto.response.PricingResponse;
import com.example.fashion_db.entity.Pricing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PricingMapper {
    @Mapping(target = "service", ignore = true)
    Pricing toPricing(PricingRequest request);

    @Mapping(target = "serviceId", source = "service.id")
    PricingResponse toPricingResponse(Pricing pricing);

    @Mapping(target = "service", ignore = true)
    void updatePricing(@MappingTarget Pricing pricing, PricingRequest request);
}