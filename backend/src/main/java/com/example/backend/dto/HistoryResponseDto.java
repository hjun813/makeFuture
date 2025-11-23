package com.example.backend.dto;

import com.example.backend.domain.History;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class HistoryResponseDto {
    private Long id;
    private String content;
    private String category;
    private LocalDateTime createdAt;

    public HistoryResponseDto(History history) {
        this.id = history.getId();
        this.content = history.getContent();
        this.category = history.getCategory();
        this.createdAt = history.getCreatedAt();
    }
}