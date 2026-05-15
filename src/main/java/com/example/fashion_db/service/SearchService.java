package com.example.fashion_db.service;

import com.example.fashion_db.dto.response.CollectionResponse;
import com.example.fashion_db.dto.response.ProductResponse;
import com.example.fashion_db.dto.response.SearchResponse;
import com.example.fashion_db.mapper.CollectionMapper;
import com.example.fashion_db.repository.CollectionRepository;
import com.example.fashion_db.repository.ProductRepository;
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
public class SearchService {

    ProductRepository productRepository;
    CollectionRepository collectionRepository;
    ProductService productService;
    CollectionMapper collectionMapper;

    public SearchResponse search(String q) {
        // Search products
        List<ProductResponse> products = productRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(q, q)
                .stream()
                .map(productService::mapProductWithImages)
                .toList();

        // Search collections
        List<CollectionResponse> collections = collectionRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(q, q)
                .stream()
                .map(collectionMapper::toCollectionResponse)
                .toList();

        return SearchResponse.builder()
                .products(products)
                .collections(collections)
                .build();
    }
}
