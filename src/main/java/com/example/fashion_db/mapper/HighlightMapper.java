package com.example.fashion_db.mapper;

import com.example.fashion_db.dto.request.HighlightRequest;
import com.example.fashion_db.dto.response.HighlightResponse;
import com.example.fashion_db.entity.Highlight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HighlightMapper {
    @Mapping(target = "service", ignore = true)
    Highlight toHighlight(HighlightRequest request);

    @Mapping(target = "serviceId", source = "service.id")
    HighlightResponse toHighlightResponse(Highlight highlight);

    @Mapping(target = "service", ignore = true)
    void updateHighlight(@MappingTarget Highlight highlight, HighlightRequest request);
}