package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.WishlistRequest;
import com.example.fashion_db.dto.response.PageResponse;
import com.example.fashion_db.dto.response.WishlistResponse;
import com.example.fashion_db.entity.Wishlist;
import com.example.fashion_db.exception.AppException;
import com.example.fashion_db.exception.ErrorCode;
import com.example.fashion_db.mapper.WishlistMapper;
import com.example.fashion_db.repository.ProductRepository;
import com.example.fashion_db.repository.UserRepository;
import com.example.fashion_db.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class WishlistService {

    WishlistRepository wishlistRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    WishlistMapper wishlistMapper;

    private String getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((Jwt) authentication.getPrincipal()).getClaim("userId");
    }

    public WishlistResponse addToWishlist(WishlistRequest request) {
        String userId = getCurrentUserId();

        if (wishlistRepository.existsByUser_IdAndProduct_Id(userId, request.getProductId()))
            throw new AppException(ErrorCode.WISHLIST_EXISTED);

        Wishlist wishlist = Wishlist.builder()
                .user(userRepository.findById(userId)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)))
                .product(productRepository.findById(request.getProductId())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)))
                .build();

        return wishlistMapper.toWishlistResponse(wishlistRepository.save(wishlist));
    }

    public PageResponse<WishlistResponse> getMyWishlist(int page, int size) {
        String userId = getCurrentUserId();
        return PageResponse.of(wishlistRepository.findByUser_Id(userId, PageRequest.of(page, size))
                .map(wishlistMapper::toWishlistResponse));
    }

    @Transactional
    public void removeFromWishlist(String productId) {
        String userId = getCurrentUserId();

        if (!wishlistRepository.existsByUser_IdAndProduct_Id(userId, productId))
            throw new AppException(ErrorCode.WISHLIST_NOT_FOUND);

        wishlistRepository.deleteByUser_IdAndProduct_Id(userId, productId);
    }

    public boolean checkWishlist(String productId) {
        String userId = getCurrentUserId();
        return wishlistRepository.existsByUser_IdAndProduct_Id(userId, productId);
    }
}