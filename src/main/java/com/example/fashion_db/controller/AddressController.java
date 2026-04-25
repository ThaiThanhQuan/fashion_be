package com.example.fashion_db.controller;


import com.example.fashion_db.dto.request.AddressRequest;
import com.example.fashion_db.dto.response.AddressResponse;
import com.example.fashion_db.dto.response.ApiResponse;
import com.example.fashion_db.service.AddressService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AddressController {
    AddressService addressService;

    @PostMapping
    ApiResponse<AddressResponse> createAddress(@RequestBody @Valid AddressRequest request) {
        return ApiResponse.<AddressResponse>builder()
                .result(addressService.createAddress(request))
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<AddressResponse>> getAllAddress() {
        return ApiResponse.<List<AddressResponse>>builder()
                .result(addressService.getAllAddress())
                .build();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<AddressResponse>> getAllAddressByUserId(@PathVariable String userId) {
        return ApiResponse.<List<AddressResponse>>builder()
                .result(addressService.getAddressesByUserId(userId))
                .build();
    }

    @PutMapping("/{addressId}")
    ApiResponse<AddressResponse> updateAddress(@PathVariable String addressId, @RequestBody @Valid AddressRequest request) {
        return ApiResponse.<AddressResponse>builder()
                .result(addressService.updateAddress(addressId, request))
                .build();
    }

    @DeleteMapping("/{addressId}")
    ApiResponse<Void> deleteAddress(@PathVariable String addressId) {
        addressService.deleteAddress(addressId);
        return ApiResponse.<Void>builder()
                .message("Delete address successfully")
                .build();
    }

}
