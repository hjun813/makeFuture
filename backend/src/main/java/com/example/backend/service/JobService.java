package com.example.backend.service;

import com.example.backend.domain.Job;
import com.example.backend.domain.User;
import com.example.backend.dto.JobCreateRequestDto;
import com.example.backend.dto.JobResponseDto;
import com.example.backend.repository.JobRepository;
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
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    // 공고 등록
    @Transactional
    public JobResponseDto createJob(JobCreateRequestDto dto, String userEmail) {
        
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Job job = new Job(
                dto.getCompanyName(),
                dto.getJobTitle(),
                dto.getUrl(),
                dto.getDeadline(),
                dto.getJobType(),
                user
        );

        Job savedJob = jobRepository.save(job);

        return new JobResponseDto(savedJob);
    }

    // 공고 조회
    public List<JobResponseDto> getMyJobs(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        List<Job> jobs = jobRepository.findAllByUserOrderByDeadlineAsc(user);

        return jobs.stream()
                .map(JobResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<JobResponseDto> getAllJobs(){
        List<Job> jobs = jobRepository.findAllByOrderByDeadlineAsc();

        return jobs.stream()
                .map(JobResponseDto::new)
                .collect(Collectors.toList());
    }

    //공고 삭제
    @Transactional 
    public void deleteJob(Long jobId, String userEmail) {
        
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("공고를 찾을 수 없습니다."));

        
        if (!job.getUser().getId().equals(user.getId())) {
        
            throw new AccessDeniedException("이 공고를 삭제할 권한이 없습니다.");
        }


        jobRepository.delete(job);
    }
}