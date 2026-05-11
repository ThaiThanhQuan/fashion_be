package com.example.fashion_db.mapper;

import com.example.fashion_db.dto.response.WishlistResponse;
import com.example.fashion_db.entity.Wishlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface WishlistMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "product", source = "product")
    WishlistResponse toWishlistResponse(Wishlist wishlist);
}