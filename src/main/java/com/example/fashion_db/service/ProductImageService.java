package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.ProductImageRequest;
import com.example.fashion_db.dto.response.PageResponse;
import com.example.fashion_db.dto.response.ProductImageResponse;
import com.example.fashion_db.entity.Product;
import com.example.fashion_db.entity.ProductImage;
import com.example.fashion_db.exception.AppException;
import com.example.fashion_db.exception.ErrorCode;
import com.example.fashion_db.mapper.ProductImageMapper;
import com.example.fashion_db.repository.ProductImageRepository;
import com.example.fashion_db.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductImageService {

    ProductImageRepository productImageRepository;
    ProductRepository productRepository;
    ProductImageMapper productImageMapper;
    CloudinaryService cloudinaryService;

    public List<ProductImageResponse> createProductImages(ProductImageRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        return request.getFiles().stream()
                .map(file -> {
                    String imageUrl = cloudinaryService.uploadImage(file);
                    ProductImage productImage = ProductImage.builder()
                            .imagePath(imageUrl)
                            .thumbnail(false)
                            .product(product)
                            .build();
                    return productImageMapper.toProductImageResponse(productImageRepository.save(productImage));
                })
                .toList();
    }

    public ProductImageResponse setThumbnail(String imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_FOUND));

        // Bỏ thumbnail cũ
        productImageRepository.findByProduct_IdAndThumbnailTrue(image.getProduct().getId())
                .ifPresent(old -> {
                    old.setThumbnail(false);
                    productImageRepository.save(old);
                });

        // Set thumbnail mới
        image.setThumbnail(true);
        return productImageMapper.toProductImageResponse(productImageRepository.save(image));
    }

    public PageResponse<ProductImageResponse> getImagesByProduct(String productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return PageResponse.of(productImageRepository.findByProduct_Id(productId, pageable)
                        .map(productImageMapper::toProductImageResponse)
                    );
    }

    public void deleteProductImage(String imageId) {
        ProductImage productImage = productImageRepository.findById(imageId)
                .orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_FOUND));

        // 1. Xóa trên Cloudinary
        cloudinaryService.deleteImage(productImage.getImagePath());

        // 2. Xóa trong DB
        productImageRepository.deleteById(imageId);
    }
}
