package com.example.fashion_db.mapper;

import com.example.fashion_db.dto.request.CollectionRequest;
import com.example.fashion_db.dto.response.CollectionResponse;
import com.example.fashion_db.entity.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CollectionMapper {
    @Mapping(target = "season", ignore = true)
    @Mapping(target = "categoryCollection", ignore = true)
    @Mapping(target = "artist", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "thumbnail", ignore = true)
    @Mapping(target = "products", ignore = true)
    Collection toCollection(CollectionRequest request);

    @Mapping(target = "seasonId", source = "season.id")
    @Mapping(target = "categoryCollectionId", source = "categoryCollection.id")
    @Mapping(target = "artistId", source = "artist.id")
    CollectionResponse toCollectionResponse(Collection collection);

    @Mapping(target = "season", ignore = true)
    @Mapping(target = "categoryCollection", ignore = true)
    @Mapping(target = "artist", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "thumbnail", ignore = true)
    @Mapping(target = "products", ignore = true)
    void updateCollection(@MappingTarget Collection collection, CollectionRequest request);
}