package com.example.fashion_db.mapper;

import com.example.fashion_db.dto.request.ArtistRequest;
import com.example.fashion_db.dto.response.ArtistResponse;
import com.example.fashion_db.entity.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    @Mapping(target = "thumbnail", ignore = true)
    Artist toArtist(ArtistRequest request);

    ArtistResponse toArtistResponse(Artist artist);

    @Mapping(target = "thumbnail", ignore = true)
    void updateArtist(@MappingTarget Artist artist, ArtistRequest request);
}