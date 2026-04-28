package com.example.fashion_db.controller;

import com.example.fashion_db.dto.request.WishlistRequest;
import com.example.fashion_db.dto.response.ApiResponse;
import com.example.fashion_db.dto.response.PageResponse;
import com.example.fashion_db.dto.response.WishlistResponse;
import com.example.fashion_db.service.WishlistService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlists")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class WishlistController {

    WishlistService wishlistService;

    @PostMapping
    public ApiResponse<WishlistResponse> addToWishlist(@RequestBody WishlistRequest request) {
        return ApiResponse.<WishlistResponse>builder()
                .result(wishlistService.addToWishlist(request))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<WishlistResponse>> getMyWishlist(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<WishlistResponse>>builder()
                .result(wishlistService.getMyWishlist(page, size))
                .build();
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<Void> removeFromWishlist(@PathVariable String productId) {
        wishlistService.removeFromWishlist(productId);
        return ApiResponse.<Void>builder()
                .message("Removed from wishlist successfully")
                .build();
    }

    @GetMapping("/{productId}")
    public ApiResponse<Boolean> checkWishlist(@PathVariable String productId) {
        return ApiResponse.<Boolean>builder()
                .result(wishlistService.checkWishlist(productId))
                .build();
    }
}
