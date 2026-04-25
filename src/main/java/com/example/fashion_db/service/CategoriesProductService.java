package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.CategoriesProductRequest;
import com.example.fashion_db.dto.response.CategoriesProductResponse;
import com.example.fashion_db.mapper.CategoriesProductMapper;
import com.example.fashion_db.repository.CategoriesProductRepository;
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
public class CategoriesProductService {

    CategoriesProductRepository categoriesProductRepository;
    CategoriesProductMapper categoriesProductMapper;


    public CategoriesProductResponse create(CategoriesProductRequest request) {
        var categories_product = categoriesProductMapper.toCategoriesProduct(request);
        categories_product = categoriesProductRepository.save(categories_product);
        return categoriesProductMapper.toCategoriesProductResponse(categories_product);
    }

    public List<CategoriesProductResponse> getAll() {
        return categoriesProductRepository.findAll().stream().map(categoriesProductMapper::toCategoriesProductResponse).toList();
    }

    public void delete(String categories_product_id) {
        categoriesProductRepository.deleteById(categories_product_id);
    }
}
