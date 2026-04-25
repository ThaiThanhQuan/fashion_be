package com.example.fashion_db.controller;

import com.example.fashion_db.dto.request.ProductVariantsCreateRequest;
import com.example.fashion_db.dto.request.ProductVariantsUpdateRequest;
import com.example.fashion_db.dto.response.ApiResponse;
import com.example.fashion_db.dto.response.PageResponse;
import com.example.fashion_db.dto.response.ProductVariantsResponse;
import com.example.fashion_db.service.ProductVariantsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product_variants")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductVariantsController {
    ProductVariantsService productVariantsService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProductVariantsResponse> createProductVariants(@RequestBody ProductVariantsCreateRequest request) {
       return ApiResponse.<ProductVariantsResponse>builder()
                .result(productVariantsService.createProductVariants(request))
                .build();
    }

    @GetMapping("/{productId}")
    public ApiResponse<PageResponse<ProductVariantsResponse>> getProductVariantsByProductId(
            @PathVariable String productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ) {
        return ApiResponse.<PageResponse<ProductVariantsResponse>>builder()
                .result(productVariantsService.getProductVariantsByProductId(productId, page, size))
                .build();
    }

    @PutMapping("/{variantId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProductVariantsResponse> updateVariant(
            @PathVariable String variantId,
            @RequestBody ProductVariantsUpdateRequest request) {
        return ApiResponse.<ProductVariantsResponse>builder()
                .result(productVariantsService.updateVariant(variantId, request))
                .build();
    }

    @DeleteMapping("/{variantId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteVariant(@PathVariable String variantId) {
        productVariantsService.deleteVariant(variantId);
        return ApiResponse.<Void>builder()
                .message("Delete variant successfully")
                .build();
    }

    @GetMapping("/size")
    public ApiResponse<PageResponse<ProductVariantsResponse>> getVariantsBySize(
            @RequestParam String size,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.<PageResponse<ProductVariantsResponse>>builder()
                .result(productVariantsService.getVariantsBySize(size, page, pageSize))
                .build();
    }

}
