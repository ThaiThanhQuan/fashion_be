package com.example.fashion_db.controller;

import com.example.fashion_db.dto.request.ProductRequest;
import com.example.fashion_db.dto.response.ApiResponse;
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

    @GetMapping
    public ApiResponse<List<ProductResponse>> getAllProduct() {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getAllProduct())
                .build();
    }

    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable String productId) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.getProductById(productId))
                .build();
    }

    @PutMapping("/{productId}")
    public ApiResponse<ProductResponse> updateProductById(@PathVariable String productId, @RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.updateProduct(productId,request))
                .build();
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<Void> deleteProductById(@PathVariable String productId) {
        productService.deleteProduct(productId);
        return ApiResponse.<Void>builder()
                .message("Delete product successfully")
                .build();
    }

    @GetMapping("/category")
    public ApiResponse<List<ProductResponse>> getProductsByCategory(@RequestParam String categoryId) {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getProductsByCategory(categoryId))
                .build();
    }

    @GetMapping("/active")
    public ApiResponse<List<ProductResponse>> getProductsByActive(@RequestParam boolean active) {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getProductsByActive(active))
                .build();
    }

    @GetMapping("/slug/{slug}")
    ApiResponse<ProductResponse> getProductBySlug(@PathVariable String slug) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.getProductBySlug(slug))
                .build();
    }

    @GetMapping("/sort")
    ApiResponse<List<ProductResponse>> getProductsSorted(@RequestParam String sortBy) {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getProductsSorted(sortBy))
                .build();
    }

    @GetMapping("/feature")
    public ApiResponse<List<ProductResponse>> getFeaturedProducts() {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getFeaturedProducts())
                .build();
    }
}
