package com.example.fashion_db.mapper;

import com.example.fashion_db.dto.request.ProductRequest;
import com.example.fashion_db.dto.response.ProductResponse;
import com.example.fashion_db.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "slug", ignore = true)
    Product toProduct(ProductRequest request);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "slug", ignore = true)
    void updateProduct(@MappingTarget Product product, ProductRequest request);

    @Mapping(target = "category_product_id", source = "category.id")
    ProductResponse toProductResponse(Product product);
}
