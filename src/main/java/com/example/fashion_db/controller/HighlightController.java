package com.example.fashion_db.controller;

import com.example.fashion_db.dto.request.HighlightRequest;
import com.example.fashion_db.dto.response.ApiResponse;
import com.example.fashion_db.dto.response.HighlightResponse;
import com.example.fashion_db.service.HighlightService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/highlights")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class HighlightController {

    HighlightService highlightService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<HighlightResponse> createHighlight(@RequestBody HighlightRequest request) {
        return ApiResponse.<HighlightResponse>builder()
                .result(highlightService.createHighlight(request))
                .build();
    }

    @GetMapping("/service/{serviceId}")
    public ApiResponse<List<HighlightResponse>> getHighlightsByService(
            @PathVariable String serviceId) {
        return ApiResponse.<List<HighlightResponse>>builder()
                .result(highlightService.getHighlightsByService(serviceId))
                .build();
    }

    @GetMapping("/{highlightId}")
    public ApiResponse<HighlightResponse> getHighlightById(@PathVariable String highlightId) {
        return ApiResponse.<HighlightResponse>builder()
                .result(highlightService.getHighlightById(highlightId))
                .build();
    }

    @PutMapping("/{highlightId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<HighlightResponse> updateHighlight(
            @PathVariable String highlightId,
            @RequestBody HighlightRequest request) {
        return ApiResponse.<HighlightResponse>builder()
                .result(highlightService.updateHighlight(highlightId, request))
                .build();
    }

    @DeleteMapping("/{highlightId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteHighlight(@PathVariable String highlightId) {
        highlightService.deleteHighlight(highlightId);
        return ApiResponse.<Void>builder()
                .message("Delete highlight successfully")
                .build();
    }
}