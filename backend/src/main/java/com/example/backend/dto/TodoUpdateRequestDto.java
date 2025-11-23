package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoUpdateRequestDto {

    @NotBlank(message = "내용은 필수입니다.")
    private String content;
}