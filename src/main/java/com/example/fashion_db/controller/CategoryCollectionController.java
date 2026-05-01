package com.example.fashion_db.controller;

import com.example.fashion_db.dto.request.CategoryCollectionRequest;
import com.example.fashion_db.dto.response.ApiResponse;
import com.example.fashion_db.dto.response.CategoryCollectionResponse;
import com.example.fashion_db.service.CategoryCollectionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category-collections")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryCollectionController {

    CategoryCollectionService categoryCollectionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CategoryCollectionResponse> createCategoryCollection(
            @RequestBody CategoryCollectionRequest request) {
        return ApiResponse.<CategoryCollectionResponse>builder()
                .result(categoryCollectionService.createCategoryCollection(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<CategoryCollectionResponse>> getAllCategoryCollections() {
        return ApiResponse.<List<CategoryCollectionResponse>>builder()
                .result(categoryCollectionService.getAllCategoryCollections())
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteCategoryCollection(@PathVariable String id) {
        categoryCollectionService.deleteCategoryCollection(id);
        return ApiResponse.<Void>builder()
                .message("Delete category collection successfully")
                .build();
    }
}