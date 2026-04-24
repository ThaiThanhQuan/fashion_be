package com.example.fashion_db.mapper;

import com.example.fashion_db.dto.request.UserRegisterRequest;
import com.example.fashion_db.dto.request.UserUpdateRequest;
import com.example.fashion_db.dto.response.UserResponse;
import com.example.fashion_db.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRegisterRequest request);

    UserResponse toUserResponse (User user);

    List<UserResponse> toUserResponseList(List<User> users);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
