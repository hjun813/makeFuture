package com.example.backend.dto;

import com.example.backend.domain.Todo;
import com.example.backend.domain.TodoCategory;
import lombok.Getter;

@Getter
public class TodoResponseDto {
    private Long id;
    private String content;
    private Boolean isDone;
    private TodoCategory category;

    public TodoResponseDto(Todo todo) {
        this.id = todo.getId();
        this.content = todo.getContent();
        this.isDone = todo.getIsDone();
        this.category = todo.getCategory();
    }
}