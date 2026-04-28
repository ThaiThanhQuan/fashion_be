package com.example.fashion_db.controller;

import com.example.fashion_db.dto.request.ProductImageRequest;
import com.example.fashion_db.dto.response.ApiResponse;
import com.example.fashion_db.dto.response.PageResponse;
import com.example.fashion_db.dto.response.ProductImageResponse;
import com.example.fashion_db.service.ProductImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product_images")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductImageController {

    ProductImageService productImageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<ProductImageResponse>> createProductImage(
            @ModelAttribute ProductImageRequest request) {
        return ApiResponse.<List<ProductImageResponse>>builder()
                .result(productImageService.createProductImages(request))
                .build();
    }

    @PatchMapping("/{imageId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProductImageResponse> setThumbnail(@PathVariable String imageId) {
        return ApiResponse.<ProductImageResponse>builder()
                .result(productImageService.setThumbnail(imageId))
                .build();
    }

    @GetMapping("/{productId}")
    public ApiResponse<PageResponse<ProductImageResponse>> getImagesByProduct(
            @PathVariable String productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<ProductImageResponse>>builder()
                .result(productImageService.getImagesByProduct(productId, page, size))
                .build();
    }

    @DeleteMapping("/{imageId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteProductImage(@PathVariable String imageId) {
        productImageService.deleteProductImage(imageId);
        return ApiResponse.<Void>builder()
                .message("Delete image successfully")
                .build();
    }
}