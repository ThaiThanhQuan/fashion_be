package com.example.fashion_db.mapper;

import com.example.fashion_db.dto.request.ProductVariantsCreateRequest;
import com.example.fashion_db.dto.request.ProductVariantsUpdateRequest;
import com.example.fashion_db.dto.response.ProductVariantsResponse;
import com.example.fashion_db.entity.ProductVariants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductVariantsMapper {
    @Mapping(target = "product", ignore = true)
    ProductVariants toProductVariant(ProductVariantsCreateRequest request);

    @Mapping(target = "product_id", source = "product.id")
    ProductVariantsResponse toProductVariantResponse(ProductVariants productVariant);

    @Mapping(target = "product", ignore = true)
    void updateProductVariant(@MappingTarget ProductVariants productVariant, ProductVariantsUpdateRequest request);
}
