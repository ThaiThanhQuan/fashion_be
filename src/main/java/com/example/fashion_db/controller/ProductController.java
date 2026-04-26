package com.example.fashion_db.controller;

import com.example.fashion_db.dto.request.ProductRequest;
import com.example.fashion_db.dto.response.ApiResponse;
import com.example.fashion_db.dto.response.PageResponse;
import com.example.fashion_db.dto.response.ProductResponse;
import com.example.fashion_db.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductController {
    ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(request))
                .build();
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProductResponse> getProductById(@PathVariable String productId) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.getProductById(productId))
                .build();
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProductResponse> updateProductById(@PathVariable String productId, @RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.updateProduct(productId,request))
                .build();
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteProductById(@PathVariable String productId) {
        productService.deleteProduct(productId);
        return ApiResponse.<Void>builder()
                .message("Delete product successfully")
                .build();
    }

    @GetMapping("/category")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PageResponse<ProductResponse>> getProductsByCategory(
            @RequestParam String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .result(productService.getProductsByCategory(categoryId,page,size))
                .build();
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PageResponse<ProductResponse>> getProductsByActive(
            @RequestParam boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .result(productService.getProductsByActive(active, page, size))
                .build();
    }

    @GetMapping("/slug/{slug}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<ProductResponse> getProductBySlug(@PathVariable String slug) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.getProductBySlug(slug))
                .build();
    }

    @GetMapping("/sort")
    ApiResponse<PageResponse<ProductResponse>> getProductsSorted(
            @RequestParam String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size ) {
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .result(productService.getProductsSorted(sortBy, page, size))
                .build();
    }

    @GetMapping("/feature")
    public ApiResponse<PageResponse<ProductResponse>> getFeaturedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .result(productService.getFeaturedProducts(page, size))
                .build();
    }

    @GetMapping("/price")
    public ApiResponse<PageResponse<ProductResponse>> getProductsByPriceRange(
            @RequestParam(defaultValue = "0") Long minPrice,
            @RequestParam(defaultValue = "999999999") Long maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .result(productService.getProductsByPriceRange(minPrice, maxPrice, page, size))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<ProductResponse>> filterProducts(
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Boolean featured,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .result(productService.filterProducts(
                        categoryId, active, featured,
                        minPrice, maxPrice, size, sortBy, page, pageSize))
                .build();
    }

    @GetMapping("/{productId}/related")
    public ApiResponse<PageResponse<ProductResponse>> getRelatedProducts(
            @PathVariable String productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .result(productService.getRelatedProducts(productId, page, size))
                .build();
    }
}
