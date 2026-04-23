package com.example.fashion_db.mapper;

import com.example.fashion_db.dto.request.UserRegisterRequest;
import com.example.fashion_db.dto.request.UserUpdateRequest;
import com.example.fashion_db.dto.response.UserResponse;
import com.example.fashion_db.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRegisterRequest request);

    UserResponse toUserResponse (User user);

    List<UserResponse> toUserResponseList(List<User> users);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
