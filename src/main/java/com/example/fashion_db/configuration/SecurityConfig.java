package com.example.fashion_db.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Đánh dấu đây là class cấu hình của Spring
@EnableWebSecurity // Bật cơ chế bảo mật của Spring Security
@EnableMethodSecurity // Phân quyền trực tiếp trên các hàm trong Service hoặc Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig {

//     CustomJwtDecoder customJwtDecoder;
//     CorsConfig corsConfig;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Cấu hình quyền truy cập API
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.POST,"/auth/register",
                                                        "/auth/login",
                                                        "/auth/refresh",
                                                        "/auth/introspec",
                                                        "/auth/logout"
                                        ).permitAll()
                .anyRequest()
                .authenticated());

        // Cấu hình xác thực JWT
//        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
//                                .decoder(customJwtDecoder)
//                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
//                        .authenticationEntryPoint(
//                                new JwtAuthenticationEntryPoint()) // xử lý những lỗi liên quan đến việc xác thực
//        );

        // Tắt CSRF (vì đang làm REST API dùng JWT, không dùng session)
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }
}
