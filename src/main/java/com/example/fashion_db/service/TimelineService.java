package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.TimelineRequest;
import com.example.fashion_db.dto.response.TimelineResponse;
import com.example.fashion_db.entity.Timeline;
import com.example.fashion_db.exception.AppException;
import com.example.fashion_db.exception.ErrorCode;
import com.example.fashion_db.mapper.TimelineMapper;
import com.example.fashion_db.repository.ServiceRepository;
import com.example.fashion_db.repository.TimelineRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TimelineService {

    TimelineRepository timelineRepository;
    ServiceRepository serviceRepository;
    TimelineMapper timelineMapper;

    public TimelineResponse createTimeline(TimelineRequest request) {
        Timeline timeline = timelineMapper.toTimeline(request);
        timeline.setService(serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND)));

        return timelineMapper.toTimelineResponse(timelineRepository.save(timeline));
    }

    public List<TimelineResponse> getTimelinesByService(String serviceId) {
        return timelineRepository.findByService_Id(serviceId)
                .stream()
                .map(timelineMapper::toTimelineResponse)
                .toList();
    }

    public TimelineResponse getTimelineById(String timelineId) {
        return timelineMapper.toTimelineResponse(
                timelineRepository.findById(timelineId)
                        .orElseThrow(() -> new AppException(ErrorCode.TIMELINE_NOT_FOUND)));
    }

    public TimelineResponse updateTimeline(String timelineId, TimelineRequest request) {
        Timeline timeline = timelineRepository.findById(timelineId)
                .orElseThrow(() -> new AppException(ErrorCode.TIMELINE_NOT_FOUND));

        timelineMapper.updateTimeline(timeline, request);
        return timelineMapper.toTimelineResponse(timelineRepository.save(timeline));
    }

    public void deleteTimeline(String timelineId) {
        timelineRepository.deleteById(timelineId);
    }
}