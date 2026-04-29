package com.example.fashion_db.controller;

import com.example.fashion_db.dto.request.SeasonRequest;
import com.example.fashion_db.dto.response.ApiResponse;
import com.example.fashion_db.dto.response.SeasonResponse;
import com.example.fashion_db.service.SeasonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seasons")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SeasonController {

    SeasonService seasonService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<SeasonResponse> createSeason(@RequestBody SeasonRequest request) {
        return ApiResponse.<SeasonResponse>builder()
                .result(seasonService.createSeason(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<SeasonResponse>> getAllSeasons() {
        return ApiResponse.<List<SeasonResponse>>builder()
                .result(seasonService.getAllSeasons())
                .build();
    }
    @DeleteMapping("/{seasonId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteSeason(@PathVariable String seasonId) {
        seasonService.deleteSeason(seasonId);
        return ApiResponse.<Void>builder()
                .message("Delete season successfully")
                .build();
    }
}