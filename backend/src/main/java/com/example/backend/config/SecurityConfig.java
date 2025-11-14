package com.example.backend.config;

// (필요한 import문이 모두 포함되어 있는지 확인하세요)
import com.example.backend.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // 1. HttpMethod 임포트
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration; // 2. CORS 관련 임포트
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List; // 3. List 임포트

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ✅ 1. CORS 정책을 정의하는 Bean을 새로 추가합니다.
    //    (이제 WebConfig.java 파일은 삭제해도 됩니다.)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:5173")); // 👈 프론트엔드 주소
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // "/api/**" 경로에 대해 위 CORS 정책을 적용합니다.
        source.registerCorsConfiguration("/api/**", config); 
        return source;
    }

    // Spring Security 설정의 핵심
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // ✅ 2. CORS 설정을 SecurityConfig에 통합합니다. (http.cors() 추가)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // (CSRF, 세션 설정은 기존과 동일)
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // ✅ 3. authorizeHttpRequests를 올바르게 수정합니다.
            .authorizeHttpRequests(auth -> auth
                
                // (1) OPTIONS 메서드(CORS Preflight)는 모두 허용
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() 
                
                // (2) 🚨 로그인/회원가입 경로 허용 (이게 빠져서 로그인이 안됐습니다)
                .requestMatchers("/api/auth/**").permitAll() 
                
                // (3) Swagger 경로 허용
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html" // (이 경로가 더 정확할 수 있습니다)
                ).permitAll()
                
                // (4) 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
            );
        
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }
}