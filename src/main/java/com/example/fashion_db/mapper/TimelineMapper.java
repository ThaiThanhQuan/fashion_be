package com.example.fashion_db.mapper;

import com.example.fashion_db.dto.request.TimelineRequest;
import com.example.fashion_db.dto.response.TimelineResponse;
import com.example.fashion_db.entity.Timeline;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TimelineMapper {
    @Mapping(target = "service", ignore = true)
    Timeline toTimeline(TimelineRequest request);

    @Mapping(target = "serviceId", source = "service.id")
    TimelineResponse toTimelineResponse(Timeline timeline);

    @Mapping(target = "service", ignore = true)
    void updateTimeline(@MappingTarget Timeline timeline, TimelineRequest request);
}