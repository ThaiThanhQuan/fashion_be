package com.example.fashion_db.controller;

import com.example.fashion_db.dto.request.CategoriesProductRequest;
import com.example.fashion_db.dto.response.ApiResponse;
import com.example.fashion_db.dto.response.CategoriesProductResponse;
import com.example.fashion_db.service.CategoriesProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category_product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoriesProductController {

    CategoriesProductService categoriesProductService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CategoriesProductResponse> createCategoriesProduct(@RequestBody CategoriesProductRequest request) {
        return ApiResponse.<CategoriesProductResponse>builder()
                .result(categoriesProductService.create(request))
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<CategoriesProductResponse>> getAllCategoriesProduct() {
        return ApiResponse.<List<CategoriesProductResponse>>builder()
                .result(categoriesProductService.getAll())
                .build();
    }

    @DeleteMapping("/{categories_product_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteCategoriesProduct(@PathVariable String categories_product_id) {
        categoriesProductService.delete(categories_product_id);
        return ApiResponse.<Void>builder()
                .message("Delete category product successfully")
                .build();
    }
}
