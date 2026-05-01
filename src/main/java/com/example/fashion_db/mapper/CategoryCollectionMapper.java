package com.example.fashion_db.mapper;

import com.example.fashion_db.dto.request.CategoryCollectionRequest;
import com.example.fashion_db.dto.response.CategoryCollectionResponse;
import com.example.fashion_db.entity.CategoryCollection;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryCollectionMapper {
    CategoryCollection toCategoryCollection(CategoryCollectionRequest request);
    CategoryCollectionResponse toCategoryCollectionResponse(CategoryCollection categoryCollection);
    void updateCategoryCollection(@MappingTarget CategoryCollection categoryCollection, CategoryCollectionRequest request);
}
