package com.example.fashion_db.controller;

import com.example.fashion_db.dto.request.UserRegisterRequest;
import com.example.fashion_db.dto.response.ApiResponse;
import com.example.fashion_db.dto.response.UserResponse;
import com.example.fashion_db.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {

    UserService userService;

    @PostMapping("/register")
    ApiResponse<UserResponse> register(@RequestBody @Valid UserRegisterRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.register(request))
                .build();
    }
}
