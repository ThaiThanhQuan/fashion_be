package com.example.fashion_db.controller;

import com.example.fashion_db.dto.request.TimelineRequest;
import com.example.fashion_db.dto.response.ApiResponse;
import com.example.fashion_db.dto.response.TimelineResponse;
import com.example.fashion_db.service.TimelineService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/timelines")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TimelineController {

    TimelineService timelineService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<TimelineResponse> createTimeline(@RequestBody TimelineRequest request) {
        return ApiResponse.<TimelineResponse>builder()
                .result(timelineService.createTimeline(request))
                .build();
    }

    @GetMapping("/service/{serviceId}")
    public ApiResponse<List<TimelineResponse>> getTimelinesByService(
            @PathVariable String serviceId) {
        return ApiResponse.<List<TimelineResponse>>builder()
                .result(timelineService.getTimelinesByService(serviceId))
                .build();
    }

    @GetMapping("/{timelineId}")
    public ApiResponse<TimelineResponse> getTimelineById(@PathVariable String timelineId) {
        return ApiResponse.<TimelineResponse>builder()
                .result(timelineService.getTimelineById(timelineId))
                .build();
    }

    @PutMapping("/{timelineId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<TimelineResponse> updateTimeline(
            @PathVariable String timelineId,
            @RequestBody TimelineRequest request) {
        return ApiResponse.<TimelineResponse>builder()
                .result(timelineService.updateTimeline(timelineId, request))
                .build();
    }

    @DeleteMapping("/{timelineId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteTimeline(@PathVariable String timelineId) {
        timelineService.deleteTimeline(timelineId);
        return ApiResponse.<Void>builder()
                .message("Delete timeline successfully")
                .build();
    }
}