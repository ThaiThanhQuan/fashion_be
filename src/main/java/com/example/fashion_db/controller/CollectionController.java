package com.example.fashion_db.controller;

import com.example.fashion_db.dto.request.CollectionRequest;
import com.example.fashion_db.dto.response.ApiResponse;
import com.example.fashion_db.dto.response.CollectionResponse;
import com.example.fashion_db.dto.response.PageResponse;
import com.example.fashion_db.service.CollectionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CollectionController {

    CollectionService collectionService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CollectionResponse> createCollection(@ModelAttribute CollectionRequest request) {
        return ApiResponse.<CollectionResponse>builder()
                .result(collectionService.createCollection(request))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<CollectionResponse>> getAllCollections(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<CollectionResponse>>builder()
                .result(collectionService.getAllCollections(page, size))
                .build();
    }

    @GetMapping("/{collectionId}")
    public ApiResponse<CollectionResponse> getCollectionById(@PathVariable String collectionId) {
        return ApiResponse.<CollectionResponse>builder()
                .result(collectionService.getCollectionById(collectionId))
                .build();
    }

    @GetMapping("/slug/{slug}")
    public ApiResponse<CollectionResponse> getCollectionBySlug(@PathVariable String slug) {
        return ApiResponse.<CollectionResponse>builder()
                .result(collectionService.getCollectionBySlug(slug))
                .build();
    }

    @GetMapping("/season/{seasonId}")
    public ApiResponse<PageResponse<CollectionResponse>> getCollectionsBySeason(
            @PathVariable String seasonId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<CollectionResponse>>builder()
                .result(collectionService.getCollectionsBySeason(seasonId, page, size))
                .build();
    }

    @GetMapping("/category/{categoryId}")
    public ApiResponse<PageResponse<CollectionResponse>> getCollectionsByCategory(
            @PathVariable String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<CollectionResponse>>builder()
                .result(collectionService.getCollectionsByCategory(categoryId, page, size))
                .build();
    }

    @GetMapping("/artist/{artistId}")
    public ApiResponse<PageResponse<CollectionResponse>> getCollectionsByArtist(
            @PathVariable String artistId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<CollectionResponse>>builder()
                .result(collectionService.getCollectionsByArtist(artistId, page, size))
                .build();
    }

    @GetMapping("/year/{year}")
    public ApiResponse<PageResponse<CollectionResponse>> getCollectionsByYear(
            @PathVariable String year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<CollectionResponse>>builder()
                .result(collectionService.getCollectionsByYear(year, page, size))
                .build();
    }

    @PutMapping(value = "/{collectionId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CollectionResponse> updateCollection(
            @PathVariable String collectionId,
            @ModelAttribute CollectionRequest request) {
        return ApiResponse.<CollectionResponse>builder()
                .result(collectionService.updateCollection(collectionId, request))
                .build();
    }

    @DeleteMapping("/{collectionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteCollection(@PathVariable String collectionId) {
        collectionService.deleteCollection(collectionId);
        return ApiResponse.<Void>builder()
                .message("Delete collection successfully")
                .build();
    }

    @GetMapping("/filters")
    public ApiResponse<PageResponse<CollectionResponse>> filterCollections(
            @RequestParam(required = false) String seasonId,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String artistId,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<CollectionResponse>>builder()
                .result(collectionService.filterCollections(
                        seasonId, categoryId, artistId, year, sortBy, page, size))
                .build();
    }
}