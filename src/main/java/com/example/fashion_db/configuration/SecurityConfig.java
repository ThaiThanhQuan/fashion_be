package com.example.fashion_db.configuration;

import com.example.fashion_db.service.OAuth2UserService;
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
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Đánh dấu đây là class cấu hình của Spring
@EnableWebSecurity // Bật cơ chế bảo mật của Spring Security
@EnableMethodSecurity // Phân quyền trực tiếp trên các hàm trong Service hoặc Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig {

     CustomJwtDecoder customJwtDecoder;
     JwtAuthenticationConfig jwtAuthenticationConfig;
     JwtAuthenticationEntryPonint jwtAuthenticationEntryPonint;
     OAuth2UserService oAuth2UserService;
     OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
        // Cấu hình quyền truy cập API
        httpSecurity.authorizeHttpRequests(request ->request
                        .requestMatchers(HttpMethod.POST,"/auth/register",
                                                        "/auth/login",
                                                        "/auth/refresh",
                                                        "/auth/introspec",
                                                        "/auth/logout",
                                                        "/auth/forgot-password",
                                                         "/auth/reset-password"
                                        ).permitAll()
                .requestMatchers(HttpMethod.GET, "/product/feature").permitAll()
                .requestMatchers(HttpMethod.GET, "/product").permitAll()
                .requestMatchers(HttpMethod.GET, "/product/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/product/slug/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/product/filter/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/collections").permitAll()
                .requestMatchers(HttpMethod.GET, "/collections/slug/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/services/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/artists/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/artists/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/category_product").permitAll()

                        .anyRequest()
                        .authenticated());

        // Cấu hình xác thực JWT
        httpSecurity.oauth2ResourceServer(
                oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConfig.jwtAuthenticationConverter())) // Kiểm tra các quyền hạn
                .authenticationEntryPoint(jwtAuthenticationEntryPonint) // xử lý những lỗi liên quan đến việc xác thực
        );

        // Cấu hình đăng nhập với OAuth2
        httpSecurity.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(oAuth2UserService)
                )
                .successHandler(oAuth2SuccessHandler)
        );

        // Tắt CSRF (vì đang làm REST API dùng JWT, không dùng session)
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

}
