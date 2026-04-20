//package com.example.fashion_db.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration // Đánh dấu đây là class cấu hình của Spring
////@EnableWebSecurity // Bật cơ chế bảo mật của Spring Security
////@EnableMethodSecurity // Phân quyền trực tiếp trên các hàm trong Service hoặc Controller
//public class SecurityConfig {
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(10);
//    }
//}
