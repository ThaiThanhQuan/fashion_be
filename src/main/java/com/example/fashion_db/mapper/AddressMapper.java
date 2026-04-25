package com.example.fashion_db.mapper;

import com.example.fashion_db.dto.request.AddressRequest;
import com.example.fashion_db.dto.response.AddressResponse;
import com.example.fashion_db.entity.Address;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address toAddress(AddressRequest request);

    @Mapping(target = "userId", source = "user.id")
    AddressResponse toAddressResponse(Address address);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", ignore = true)
    void updateAddress(@MappingTarget Address address, AddressRequest request);
}
