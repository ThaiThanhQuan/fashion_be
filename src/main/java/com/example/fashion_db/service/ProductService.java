package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.ProductRequest;
import com.example.fashion_db.dto.response.ProductResponse;
import com.example.fashion_db.entity.Product;
import com.example.fashion_db.exception.AppException;
import com.example.fashion_db.exception.ErrorCode;
import com.example.fashion_db.mapper.ProductMapper;
import com.example.fashion_db.repository.CategoriesProductRepository;
import com.example.fashion_db.repository.ProductRepository;
import com.example.fashion_db.utils.SlugUtils;
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

    public List<ProductResponse> getAllProduct(){
        return productRepository.findAll().stream()
                .map(productMapper::toProductResponse)
                .toList();
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

    public List<ProductResponse> getProductsByCategory(String categoryId) {
        return productRepository.findByCategory_Id(categoryId)
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public List<ProductResponse> getProductsByActive(boolean active) {
        return productRepository.findByActive(active)
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public ProductResponse getProductBySlug(String slug) {
        return productMapper.toProductResponse(
                productRepository.findBySlug(slug)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED))
        );
    }

    public List<ProductResponse> getProductsSorted(String sortBy) {
        List<Product> products = switch (sortBy) {
            case "newest"     -> productRepository.findAllByOrderByCreatedAtDesc();
            case "price_asc"  -> productRepository.findAllByOrderByPriceAsc();
            case "price_desc" -> productRepository.findAllByOrderByPriceDesc();
            default           -> productRepository.findAll();
        };

        return products.stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public List<ProductResponse> getFeaturedProducts() {
        return productRepository.findByFeatured(true)
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }
}
