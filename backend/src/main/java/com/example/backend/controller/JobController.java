package com.example.backend.controller;

import com.example.backend.dto.JobCreateRequestDto;
import com.example.backend.dto.JobResponseDto;
import com.example.backend.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    
    //새 공고 등록
    
    @PostMapping
    public ResponseEntity<JobResponseDto> createJob(@Valid @RequestBody JobCreateRequestDto dto,
                                                    Authentication authentication) {
        
        String email = authentication.getName();

        JobResponseDto createdJob = jobService.createJob(dto, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
    }

    //공고 목록 조회

    @GetMapping
    public ResponseEntity<List<JobResponseDto>> getMyJobs(Authentication authentication) {
        String email = authentication.getName();
        List<JobResponseDto> myJobs = jobService.getMyJobs(email);
        return ResponseEntity.ok(myJobs);
    }

    //공고삭제 

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long jobId,
                                          Authentication authentication) {
        String email = authentication.getName();
        
        try {
            jobService.deleteJob(jobId, email);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException e) {
            
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (RuntimeException e) {
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<JobResponseDto>> getAllJobs() {
        List<JobResponseDto> allJobs = jobService.getAllJobs();
        return ResponseEntity.ok(allJobs);
    }
    
}