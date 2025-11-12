package com.example.backend.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("토큰 생성 및 검증")
    void 토큰_생성_검증() {
        String testEmail = "test@test.com";

        String token = jwtUtil.createToken(testEmail);

        assertThat(token).isNotNull().isNotBlank();

        assertThat(jwtUtil.validateToken(token)).isTrue();
    }

    @Test
    @DisplayName("토큰에서 이메일 추출")
    void 토큰_이메일_추출() {

        String testEmail = "user123@google.com";
        String token = jwtUtil.createToken(testEmail);
        String extractedEmail = jwtUtil.getEmailFromToken(token);

        assertThat(extractedEmail).isEqualTo(testEmail);
    }

    @Test
    @DisplayName("유효하지 않은 토큰 검증")
    void 토큰_유효성_검증_유효하지_않음() {
        String invalidToken = "this.is.not.a.valid.jwt.token";
        boolean isValid = jwtUtil.validateToken(invalidToken);

        assertThat(isValid).isFalse();
    }
}