package com.example.fashion_db.mapper;

import com.example.fashion_db.dto.response.WishlistResponse;
import com.example.fashion_db.entity.Wishlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WishlistMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "productId", source = "product.id")
    WishlistResponse toWishlistResponse(Wishlist wishlist);
}