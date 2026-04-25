package com.example.fashion_db.mapper;


import com.example.fashion_db.dto.request.CategoriesProductRequest;
import com.example.fashion_db.dto.response.CategoriesProductResponse;
import com.example.fashion_db.entity.CategoriesProduct;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriesProductMapper {

    CategoriesProduct toCategoriesProduct(CategoriesProductRequest request);

    CategoriesProductResponse toCategoriesProductResponse(CategoriesProduct role);
}
