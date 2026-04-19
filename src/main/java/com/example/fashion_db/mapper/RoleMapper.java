package com.example.fashion_db.mapper;

import com.example.fashion_db.dto.request.RoleRequest;
import com.example.fashion_db.dto.response.RoleResponse;
import com.example.fashion_db.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
