package com.example.backend.controller;

import com.example.backend.dto.HistoryCreateRequestDto;
import com.example.backend.dto.HistoryResponseDto;
import com.example.backend.dto.HistoryUpdateRequestDto;
import com.example.backend.service.HistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/histories")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    // 조회
    @GetMapping
    public ResponseEntity<List<HistoryResponseDto>> getMyHistories(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(historyService.getMyHistories(email));
    }

    // 생성
    @PostMapping
    public ResponseEntity<HistoryResponseDto> createHistory(@Valid @RequestBody HistoryCreateRequestDto dto,
                                                            Authentication authentication) {
        String email = authentication.getName();
        HistoryResponseDto createdHistory = historyService.createHistory(dto, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHistory);
    }

    // 수정
    @PutMapping("/{historyId}")
    public ResponseEntity<HistoryResponseDto> updateHistory(@PathVariable Long historyId,
                                                            @Valid @RequestBody HistoryUpdateRequestDto dto,
                                                            Authentication authentication) {
        String email = authentication.getName();
        HistoryResponseDto updatedHistory = historyService.updateHistory(historyId, dto, email);
        return ResponseEntity.ok(updatedHistory);
    }

    // 삭제
    @DeleteMapping("/{historyId}")
    public ResponseEntity<Void> deleteHistory(@PathVariable Long historyId,
                                                Authentication authentication) {
        String email = authentication.getName();
        historyService.deleteHistory(historyId, email);
        return ResponseEntity.noContent().build(); 
    }
}