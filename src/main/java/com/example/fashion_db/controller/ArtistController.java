package com.example.fashion_db.controller;

import com.example.fashion_db.dto.request.ArtistRequest;
import com.example.fashion_db.dto.response.ApiResponse;
import com.example.fashion_db.dto.response.ArtistResponse;
import com.example.fashion_db.dto.response.PageResponse;
import com.example.fashion_db.service.ArtistService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/artists")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ArtistController {

    ArtistService artistService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ArtistResponse> createArtist(@ModelAttribute ArtistRequest request) {
        return ApiResponse.<ArtistResponse>builder()
                .result(artistService.createArtist(request))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<ArtistResponse>> getAllArtists(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ArtistResponse>>builder()
                .result(artistService.getAllArtists(page, size))
                .build();
    }

    @GetMapping("/{artistId}")
    public ApiResponse<ArtistResponse> getArtistById(@PathVariable String artistId) {
        return ApiResponse.<ArtistResponse>builder()
                .result(artistService.getArtistById(artistId))
                .build();
    }

    @PutMapping(value = "/{artistId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ArtistResponse> updateArtist(
            @PathVariable String artistId,
            @ModelAttribute ArtistRequest request) {
        return ApiResponse.<ArtistResponse>builder()
                .result(artistService.updateArtist(artistId, request))
                .build();
    }

    @DeleteMapping("/{artistId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteArtist(@PathVariable String artistId) {
        artistService.deleteArtist(artistId);
        return ApiResponse.<Void>builder()
                .message("Delete artist successfully")
                .build();
    }
}