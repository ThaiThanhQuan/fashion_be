package com.example.fashion_db.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;

    String email;
    String password;
    LocalDate dob;
    boolean gender;
    boolean isActive;
    LocalDateTime created_at;

    Set<RoleResponse> roles;
}
