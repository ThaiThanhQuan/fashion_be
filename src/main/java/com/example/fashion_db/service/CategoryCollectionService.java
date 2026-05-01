package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.CategoryCollectionRequest;
import com.example.fashion_db.dto.response.CategoryCollectionResponse;
import com.example.fashion_db.entity.CategoryCollection;
import com.example.fashion_db.exception.AppException;
import com.example.fashion_db.exception.ErrorCode;
import com.example.fashion_db.mapper.CategoryCollectionMapper;
import com.example.fashion_db.repository.CategoryCollectionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryCollectionService {

    CategoryCollectionRepository categoryCollectionRepository;
    CategoryCollectionMapper categoryCollectionMapper;

    public CategoryCollectionResponse createCategoryCollection(CategoryCollectionRequest request) {
        if (categoryCollectionRepository.existsByName(request.getName()))
            throw new AppException(ErrorCode.CATEGORY_COLLECTION_EXISTED);

        return categoryCollectionMapper.toCategoryCollectionResponse(
                categoryCollectionRepository.save(
                        categoryCollectionMapper.toCategoryCollection(request)));
    }

    public List<CategoryCollectionResponse> getAllCategoryCollections() {
        return categoryCollectionRepository.findAll()
                .stream()
                .map(categoryCollectionMapper::toCategoryCollectionResponse)
                .toList();
    }

    public void deleteCategoryCollection(String id) {
        categoryCollectionRepository.deleteById(id);
    }
}