package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.WishlistRequest;
import com.example.fashion_db.dto.response.ProductResponse;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class WishlistService {

    WishlistRepository wishlistRepository;
    ProductRepository productRepository;
    ProductService productService;
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

        WishlistResponse response = wishlistMapper.toWishlistResponse(wishlistRepository.save(wishlist));
        response.setProduct(productService.mapProductWithImages(wishlist.getProduct()));
        return response;
    }

    public List<WishlistResponse> getAllMyWishlist() {
        String userId = getCurrentUserId();
        return wishlistRepository.findByUser_Id(userId)
                .stream()
                .map(wishlist -> {
                    WishlistResponse response = wishlistMapper.toWishlistResponse(wishlist);
                    ProductResponse product = productService.mapProductWithImages(wishlist.getProduct());
                    response.setProduct(product);
                    return response;
                })
                .toList();
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
