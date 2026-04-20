package com.example.fashion_db.mapper;

import com.example.fashion_db.dto.request.UserRegisterRequest;
import com.example.fashion_db.dto.response.UserResponse;
import com.example.fashion_db.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRegisterRequest request);

    UserResponse toUserResponse (User user);
}
