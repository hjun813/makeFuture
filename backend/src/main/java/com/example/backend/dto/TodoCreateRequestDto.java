package com.example.backend.dto;

import com.example.backend.domain.TodoCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoCreateRequestDto {

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @NotNull(message = "카테고리는 필수입니다. (TODAY, WEEK, MONTH)")
    private TodoCategory category;
}