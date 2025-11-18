package com.example.backend.service;

import com.example.backend.domain.History;
import com.example.backend.domain.User;
import com.example.backend.dto.HistoryCreateRequestDto;
import com.example.backend.dto.HistoryResponseDto;
import com.example.backend.dto.HistoryUpdateRequestDto;
import com.example.backend.repository.HistoryRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;


    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }


    private History findHistoryByIdAndUser(Long historyId, User user) {
        History history = historyRepository.findById(historyId)
                .orElseThrow(() -> new RuntimeException("히스토리를 찾을 수 없습니다."));

        if (!history.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("이 히스토리에 접근할 권한이 없습니다.");
        }
        return history;
    }

    // 히스토리 조회

    public List<HistoryResponseDto> getMyHistories(String userEmail) {
        User user = getUserByEmail(userEmail);
        List<History> histories = historyRepository.findAllByUserOrderByCreatedAtDesc(user);

        return histories.stream()
                .map(HistoryResponseDto::new)
                .collect(Collectors.toList());
    }

    // 생성
    @Transactional
    public HistoryResponseDto createHistory(HistoryCreateRequestDto dto, String userEmail) {
        User user = getUserByEmail(userEmail);
        History history = new History(dto.getContent(), dto.getCategory(), user);
        History savedHistory = historyRepository.save(history);
        return new HistoryResponseDto(savedHistory);
    }

    // 수정
    @Transactional
    public HistoryResponseDto updateHistory(Long historyId, HistoryUpdateRequestDto dto, String userEmail) {
        User user = getUserByEmail(userEmail);
        History history = findHistoryByIdAndUser(historyId, user); // 소유권 검증 포함

        history.update(dto.getContent(), dto.getCategory()); // 엔티티 변경 (변경 감지)
        
        return new HistoryResponseDto(history);
    }

    // 삭제
    @Transactional
    public void deleteHistory(Long historyId, String userEmail) {
        User user = getUserByEmail(userEmail);
        History history = findHistoryByIdAndUser(historyId, user); // 소유권 검증 포함

        historyRepository.delete(history);
    }
}