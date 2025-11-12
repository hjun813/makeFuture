package com.example.backend.service;

import com.example.backend.domain.User;
import com.example.backend.dto.JwtResponseDto;
import com.example.backend.dto.LoginRequestDto;
import com.example.backend.dto.SignupRequestDto;
import com.example.backend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private SignupRequestDto signupRequestDto;

    @BeforeEach
    void setUp() {
        
        signupRequestDto = new SignupRequestDto();
        signupRequestDto.setEmail("test@test.com");
        signupRequestDto.setUsername("테스터");
        signupRequestDto.setPassword("1234");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void 회원가입_테스트() {
        
        authService.signup(signupRequestDto);

        User savedUser = userRepository.findByEmail("test@test.com")
                .orElseThrow(() -> new AssertionError("회원가입 실패: 사용자를 찾을 수 없습니다."));

        assertThat(savedUser.getEmail()).isEqualTo("test@test.com");
        assertThat(savedUser.getUsername()).isEqualTo("테스터");
        

        assertThat(savedUser.getPassword()).isNotEqualTo("1234");

        assertThat(passwordEncoder.matches("1234", savedUser.getPassword())).isTrue();
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 이메일")
    void 회원가입_테스트_실패_중복된_이메일() {

        authService.signup(signupRequestDto);

        SignupRequestDto duplicateDto = new SignupRequestDto();
        duplicateDto.setEmail("test@test.com");
        duplicateDto.setUsername("다른테스터");
        duplicateDto.setPassword("5678");

        assertThatThrownBy(() -> {
            authService.signup(duplicateDto);
        })
        .isInstanceOf(RuntimeException.class) 
        .hasMessageContaining("이미 존재하는 이메일입니다."); 
    }

    @Test
    @DisplayName("로그인 성공")
    void 로그인_테스트() {

        authService.signup(signupRequestDto);

        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setEmail("test@test.com");
        loginDto.setPassword("1234");

        JwtResponseDto responseDto = authService.login(loginDto);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getToken()).isNotBlank();
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 이메일")
    void 로그인_테스트_실패_존재하지_않는_이메일() {
        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setEmail("wrong@test.com");
        loginDto.setPassword("1234");

        assertThatThrownBy(() -> {
            authService.login(loginDto);
        })
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void 로그인_테스트_실패_잘못된_비밀번호() {
        authService.signup(signupRequestDto);

        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setEmail("test@test.com");
        loginDto.setPassword("wrong-password");

        assertThatThrownBy(() -> {
            authService.login(loginDto);
        })
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("비밀번호가 일치하지 않습니다.");
    }
}