package com.example.fashion_db.service;

import com.example.fashion_db.entity.Role;
import com.example.fashion_db.entity.User;
import com.example.fashion_db.repository.RoleRepository;
import com.example.fashion_db.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OAuth2UserService extends DefaultOAuth2UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        OAuth2User oAuth2User = super.loadUser(request);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String avatar = oAuth2User.getAttribute("picture");

        // Tìm hoặc tạo user
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createUser(email, name, avatar));

        return oAuth2User;
    }

    private User createUser(String email, String name, String avatar) {
        HashSet<Role> roles = new HashSet<>();
        roleRepository.findByName("USER")
                .ifPresent(roles::add);

        User user = User.builder()
                .email(email)
                .username(name)
                .avatar(avatar)
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .active(true)
                .roles(roles)
                .build();

        return userRepository.save(user);
    }
}