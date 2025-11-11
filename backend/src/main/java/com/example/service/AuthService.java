package com.example.service;

import com.example.dto.JwtResponseDto;
import com.example.dto.LoginRequestDto;
import com.example.dto.SignupRequestDto;
import com.example.domain.User;
import com.example.jwt.JwtUtil;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void signup(SignupRequestDto requestDto) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // 사용자 생성 및 저장
        User user = new User(
                requestDto.getEmail(),
                encodedPassword,
                requestDto.getUsername()
        );

        userRepository.save(user);
    }

    public JwtResponseDto login(LoginRequestDto requestDto) {
        // 이메일 조회
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 생성
        String token = jwtUtil.createToken(user.getEmail());

        return new JwtResponseDto(token);
    }
}
