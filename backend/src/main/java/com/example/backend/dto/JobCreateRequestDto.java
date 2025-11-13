package com.example.backend.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class JobCreateRequestDto {

    @NotBlank(message = "회사명은 필수입니다.")
    private String companyName;

    @NotBlank(message = "직무는 필수입니다.")
    private String jobTitle;

    private String url; // 공고 URL

    @Future(message = "마감일은 오늘 이후의 날짜여야 합니다.")
    private LocalDate deadline; // 마감일

    private String jobType; // (신입/인턴 등)
}