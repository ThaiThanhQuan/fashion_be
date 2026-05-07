package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.HighlightRequest;
import com.example.fashion_db.dto.response.HighlightResponse;
import com.example.fashion_db.entity.Highlight;
import com.example.fashion_db.exception.AppException;
import com.example.fashion_db.exception.ErrorCode;
import com.example.fashion_db.mapper.HighlightMapper;
import com.example.fashion_db.repository.HighlightRepository;
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
public class HighlightService {

    HighlightRepository highlightRepository;
    ServiceRepository serviceRepository;
    HighlightMapper highlightMapper;

    public HighlightResponse createHighlight(HighlightRequest request) {
        Highlight highlight = highlightMapper.toHighlight(request);
        highlight.setService(serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND)));

        return highlightMapper.toHighlightResponse(highlightRepository.save(highlight));
    }

    public List<HighlightResponse> getHighlightsByService(String serviceId) {
        return highlightRepository.findByService_Id(serviceId)
                .stream()
                .map(highlightMapper::toHighlightResponse)
                .toList();
    }

    public HighlightResponse getHighlightById(String highlightId) {
        return highlightMapper.toHighlightResponse(
                highlightRepository.findById(highlightId)
                        .orElseThrow(() -> new AppException(ErrorCode.HIGHLIGHT_NOT_FOUND)));
    }

    public HighlightResponse updateHighlight(String highlightId, HighlightRequest request) {
        Highlight highlight = highlightRepository.findById(highlightId)
                .orElseThrow(() -> new AppException(ErrorCode.HIGHLIGHT_NOT_FOUND));

        highlightMapper.updateHighlight(highlight, request);
        return highlightMapper.toHighlightResponse(highlightRepository.save(highlight));
    }

    public void deleteHighlight(String highlightId) {
        highlightRepository.deleteById(highlightId);
    }
}