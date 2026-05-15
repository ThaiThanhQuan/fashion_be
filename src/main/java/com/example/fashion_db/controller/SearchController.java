package com.example.fashion_db.controller;

import com.example.fashion_db.dto.response.ApiResponse;
import com.example.fashion_db.dto.response.SearchResponse;
import com.example.fashion_db.service.SearchService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SearchController {

    SearchService searchService;

    @GetMapping
    public ApiResponse<SearchResponse> search(@RequestParam String q) {
        return ApiResponse.<SearchResponse>builder()
                .result(searchService.search(q))
                .build();
    }
}
