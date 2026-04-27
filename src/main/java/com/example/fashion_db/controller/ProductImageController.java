package com.example.fashion_db.controller;

import com.example.fashion_db.dto.response.ApiResponse;
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

@RestController
@RequestMapping("/product-images")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductImageController {

    ProductImageService productImageService;

    // Dùng MultipartFile thay vì @RequestBody
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProductImageResponse> createProductImage(
            @RequestParam String productId,
            @RequestParam("file") MultipartFile file) {
        return ApiResponse.<ProductImageResponse>builder()
                .result(productImageService.createProductImage(productId, file))
                .build();
    }

//    @GetMapping("/product/{productId}")
//    public ApiResponse<List<ProductImageResponse>> getImagesByProduct(
//            @PathVariable String productId) {
//        return ApiResponse.<List<ProductImageResponse>>builder()
//                .result(productImageService.getImagesByProduct(productId))
//                .build();
//    }

    @DeleteMapping("/{imageId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteProductImage(@PathVariable String imageId) {
        productImageService.deleteProductImage(imageId);
        return ApiResponse.<Void>builder()
                .message("Delete image successfully")
                .build();
    }
}