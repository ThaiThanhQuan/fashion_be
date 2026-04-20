package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.UserRegisterRequest;
import com.example.fashion_db.dto.response.UserResponse;
import com.example.fashion_db.entity.User;
import com.example.fashion_db.entity.Role;
import com.example.fashion_db.mapper.UserMapper;
import com.example.fashion_db.repository.RoleRepository;
import com.example.fashion_db.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
//    PasswordEncoder passwordEncoder;

    public UserResponse register(UserRegisterRequest request) {
        User user = userMapper.toUser(request);
//        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        roles.add(userRole);
        user.setRoles(roles);

        user = userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

}
