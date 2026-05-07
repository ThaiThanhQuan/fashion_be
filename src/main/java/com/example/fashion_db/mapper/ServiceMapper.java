package com.example.fashion_db.mapper;

import com.example.fashion_db.dto.request.ServiceRequest;
import com.example.fashion_db.dto.response.ServiceResponse;
import com.example.fashion_db.entity.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    @Mapping(target = "artist", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "thumbnail", ignore = true)
    Service toService(ServiceRequest request);

    @Mapping(target = "artistId", source = "artist.id")
    ServiceResponse toServiceResponse(Service service);

    @Mapping(target = "artist", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "thumbnail", ignore = true)
    void updateService(@MappingTarget Service service, ServiceRequest request);
}