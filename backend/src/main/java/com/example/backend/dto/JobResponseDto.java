package com.example.backend.dto;

import com.example.backend.domain.Job;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class JobResponseDto {
    private Long id;
    private String companyName;
    private String jobTitle;
    private String url;
    private LocalDate deadline;
    private String jobType;

    public JobResponseDto(Job job) {
        this.id = job.getId();
        this.companyName = job.getCompanyName();
        this.jobTitle = job.getJobTitle();
        this.url = job.getUrl();
        this.deadline = job.getDeadline();
        this.jobType = job.getJobType();
    }
}