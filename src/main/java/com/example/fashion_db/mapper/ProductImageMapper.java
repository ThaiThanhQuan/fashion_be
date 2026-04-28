package com.example.fashion_db.mapper;

import com.example.fashion_db.dto.request.ProductImageRequest;
import com.example.fashion_db.dto.response.ProductImageResponse;
import com.example.fashion_db.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {
    @Mapping(target = "productId", source = "product.id")
    ProductImageResponse toProductImageResponse(ProductImage productImage);

    @Mapping(target = "product", ignore = true)
    void updateProductImage(@MappingTarget ProductImage productImage, ProductImageRequest request);
}