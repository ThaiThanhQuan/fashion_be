package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.UserUpdateRequest;
import com.example.fashion_db.dto.response.UserResponse;
import com.example.fashion_db.entity.User;
import com.example.fashion_db.mapper.UserMapper;
import com.example.fashion_db.repository.RoleRepository;
import com.example.fashion_db.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public List<UserResponse> getAllUser() {
        return userMapper.toUserResponseList(userRepository.findAll());
    }

    public UserResponse updateUser(String userID, UserUpdateRequest request) {
        User user = userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User not found"));

        // Kiểm tra nếu người dùng hiện tại KHÔNG phải Admin
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !user.getUsername().equals(currentUsername)) {
            throw new RuntimeException("You do not have the right to edit other people's profiles.!");
        }

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (isAdmin) {
            if (request.getRoles() != null) {
                var roles = roleRepository.findAllByNameIn(request.getRoles());
                user.setRoles(new HashSet<>(roles));
            }

            user.setActive(request.isActive());
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

}
