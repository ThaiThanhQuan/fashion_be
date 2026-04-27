package com.example.fashion_db.service;

import com.example.fashion_db.dto.response.ProductImageResponse;
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

    public ProductImageResponse createProductImage(String productId, MultipartFile file) {
        // 1. Upload lên Cloudinary → lấy URL
        String imageUrl = cloudinaryService.uploadImage(file);

        // 2. Lưu URL vào DB
        ProductImage productImage = ProductImage.builder()
                .imagePath(imageUrl)
                .product(productRepository.findById(productId)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)))
                .build();

        return productImageMapper.toProductImageResponse(productImageRepository.save(productImage));
    }

//    public List<ProductImageResponse> getImagesByProduct(String productId) {
//        return productImageRepository.findByProduct_Id(productId)
//                .stream()
//                .map(productImageMapper::toProductImageResponse)
//                .toList();
//    }

    public void deleteProductImage(String imageId) {
        ProductImage productImage = productImageRepository.findById(imageId)
                .orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_FOUND));

        // 1. Xóa trên Cloudinary
        cloudinaryService.deleteImage(productImage.getImagePath());

        // 2. Xóa trong DB
        productImageRepository.deleteById(imageId);
    }
}
