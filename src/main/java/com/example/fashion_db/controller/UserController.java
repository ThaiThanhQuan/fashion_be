package com.example.fashion_db.controller;

import com.example.fashion_db.dto.request.UserUpdateRequest;
import com.example.fashion_db.dto.response.ApiResponse;
import com.example.fashion_db.dto.response.UserResponse;
import com.example.fashion_db.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {

    UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<UserResponse>> getAllUser() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUser())
                .build();
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<UserResponse> adminUpdateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.adminUpdateUser(userId, request))
                .build();
    }

    @PutMapping("/myInfo")
    @PreAuthorize("isAuthenticated()")
    ApiResponse<UserResponse> updateMyInfo(@RequestBody @Valid UserUpdateRequest request,  @AuthenticationPrincipal Jwt jwt) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateMyInfo(request, jwt.getSubject()))
                .build();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<UserResponse> getUserById(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserById(userId))
                .build();
    }

    @GetMapping("/myInfo")
    @PreAuthorize("isAuthenticated()")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .result("Delete user success")
                .build();
    }

}
