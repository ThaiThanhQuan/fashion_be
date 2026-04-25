package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.ProductVariantsCreateRequest;
import com.example.fashion_db.dto.request.ProductVariantsUpdateRequest;
import com.example.fashion_db.dto.response.PageResponse;
import com.example.fashion_db.dto.response.ProductVariantsResponse;
import com.example.fashion_db.entity.ProductVariants;
import com.example.fashion_db.exception.AppException;
import com.example.fashion_db.exception.ErrorCode;
import com.example.fashion_db.mapper.ProductVariantsMapper;
import com.example.fashion_db.repository.ProductRepository;
import com.example.fashion_db.repository.ProductVariantsRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductVariantsService {

    ProductVariantsRepository productVariantsRepository;
    ProductRepository productRepository;
    ProductVariantsMapper productVariantsMapper;

    public ProductVariantsResponse createProductVariants(ProductVariantsCreateRequest request){
        ProductVariants variant = productVariantsMapper.toProductVariant(request);
        variant.setProduct(productRepository.findById(request.getProduct_id())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)));

        return productVariantsMapper.toProductVariantResponse(productVariantsRepository.save(variant));
    }

    public PageResponse<ProductVariantsResponse> getProductVariantsByProductId(String productId, int page, int size){
        Pageable pageable = PageRequest.of(page,size);

        return PageResponse.of(productVariantsRepository.findByProduct_Id(productId, pageable)
                .map(productVariantsMapper::toProductVariantResponse));

    }

    public PageResponse<ProductVariantsResponse> getVariantsBySize(String size, int page, int size_) {
        return PageResponse.of(productVariantsRepository.findBySize(size, PageRequest.of(page, size_))
                .map(productVariantsMapper::toProductVariantResponse));
    }

    public ProductVariantsResponse updateVariant(String variantId, ProductVariantsUpdateRequest request) {
        ProductVariants variant = productVariantsRepository.findById(variantId)
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_FOUND));

        boolean sizeExisted = productVariantsRepository
                .existsByProduct_IdAndSizeAndIdNot(variant.getProduct().getId(), request.getSize(), variantId);

        if (sizeExisted)
            throw new AppException(ErrorCode.VARIANT_SIZE_EXISTED);

        productVariantsMapper.updateProductVariant(variant, request);

        return productVariantsMapper.toProductVariantResponse(productVariantsRepository.save(variant));
    }

    public void deleteVariant(String variantId) {
        productVariantsRepository.deleteById(variantId);
    }
}
