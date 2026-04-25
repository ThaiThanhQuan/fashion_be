package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.ProductRequest;
import com.example.fashion_db.dto.response.PageResponse;
import com.example.fashion_db.dto.response.ProductResponse;
import com.example.fashion_db.entity.Product;
import com.example.fashion_db.exception.AppException;
import com.example.fashion_db.exception.ErrorCode;
import com.example.fashion_db.mapper.ProductMapper;
import com.example.fashion_db.repository.CategoriesProductRepository;
import com.example.fashion_db.repository.ProductRepository;
import com.example.fashion_db.specification.ProductSpecification;
import com.example.fashion_db.utils.SlugUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductService {

    ProductRepository productRepository;
    CategoriesProductRepository categoriesProductRepository;
    ProductMapper productMapper;

    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsByTitle(request.getTitle()))
            throw new AppException(ErrorCode.PRODUCT_EXISTED);

        Product product = productMapper.toProduct(request);
        product.setSlug(SlugUtils.generateSlug(request.getTitle()));
        product.setCategory(categoriesProductRepository.findById(request.getCategory_product())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)));

        return productMapper.toProductResponse(productRepository.save(product));
    }

    public PageResponse<ProductResponse> getAllProduct(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return PageResponse.of(productRepository.findAll(pageable)
                .map(productMapper::toProductResponse));
    }

    public ProductResponse getProductById(String productId) {
        return productMapper.toProductResponse(
                productRepository.findById(productId)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)));
    }

    public ProductResponse updateProduct(String productId, ProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        productMapper.updateProduct(product, request);
        product.setSlug(SlugUtils.generateSlug(request.getTitle()));
        product.setCategory(categoriesProductRepository.findById(request.getCategory_product())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)));

        return productMapper.toProductResponse(productRepository.save(product));
    }

    public void deleteProduct(String productId) {
        productRepository.deleteById(productId);
    }

    public PageResponse<ProductResponse> getProductsByCategory(String categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return PageResponse.of(productRepository.findByCategory_Id(categoryId, pageable)
                .map(productMapper::toProductResponse));
    }

    public PageResponse<ProductResponse> getProductsByActive(boolean active, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return PageResponse.of(productRepository.findByActive(active, pageable)
                .map(productMapper::toProductResponse));
    }

    public ProductResponse getProductBySlug(String slug) {
        return productMapper.toProductResponse(
                productRepository.findBySlug(slug)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED))
        );
    }

    public PageResponse<ProductResponse> getProductsSorted(String sortBy, int page, int size) {
        Sort sort = switch (sortBy) {
            case "price_asc"  -> Sort.by("price").ascending();
            case "price_desc" -> Sort.by("price").descending();
            case "newest"     -> Sort.by("createdAt").descending();
            default           -> Sort.unsorted();
        };

        Pageable pageable = PageRequest.of(page, size, sort);
        return PageResponse.of(productRepository.findAll(pageable)
                .map(productMapper::toProductResponse));
    }

    public PageResponse<ProductResponse> getFeaturedProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return PageResponse.of(productRepository.findByFeatured(true, pageable)
                        .map(productMapper::toProductResponse));
    }

    public PageResponse<ProductResponse> getProductsByPriceRange(Long minPrice, Long maxPrice, int page, int size) {
        return PageResponse.of(productRepository.findByPriceBetween(minPrice, maxPrice, PageRequest.of(page, size))
                .map(productMapper::toProductResponse));
    }

    public PageResponse<ProductResponse> filterProducts(
            String categoryId,
            Boolean active,
            Boolean featured,
            Long minPrice,
            Long maxPrice,
            String size,
            String sortBy,
            int page,
            int pageSize) {

        Sort sort = switch (sortBy != null ? sortBy : "") {
            case "price_asc"  -> Sort.by("price").ascending();
            case "price_desc" -> Sort.by("price").descending();
            case "newest"     -> Sort.by("createdAt").descending();
            default           -> Sort.unsorted();
        };

        Specification<Product> spec = Specification
                .where(ProductSpecification.hasCategory(categoryId))
                .and(ProductSpecification.hasActive(active))
                .and(ProductSpecification.hasFeatured(featured))
                .and(ProductSpecification.hasPriceBetween(minPrice, maxPrice))
                .and(ProductSpecification.hasSize(size));

        return PageResponse.of(productRepository.findAll(spec, PageRequest.of(page, pageSize, sort))
                .map(productMapper::toProductResponse));
    }

}
