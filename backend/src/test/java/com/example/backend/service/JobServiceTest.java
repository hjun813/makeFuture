package com.example.backend.service;

import com.example.backend.domain.Job;
import com.example.backend.domain.User;
import com.example.backend.dto.JobCreateRequestDto;
import com.example.backend.dto.JobResponseDto;
import com.example.backend.dto.SignupRequestDto;
import com.example.backend.repository.JobRepository;
import com.example.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class JobServiceTest {

    @Autowired
    private JobService jobService;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService; 

    private User userA;
    private User userB;
    private JobCreateRequestDto jobDto;

   
    @BeforeEach
    void setUp() {
        // 사용자 A 생성
        SignupRequestDto userADto = new SignupRequestDto();
        userADto.setEmail("userA@test.com");
        userADto.setUsername("UserA");
        userADto.setPassword("1234");
        authService.signup(userADto);
        userA = userRepository.findByEmail("userA@test.com").get();

        // 사용자 B 생성
        SignupRequestDto userBDto = new SignupRequestDto();
        userBDto.setEmail("userB@test.com");
        userBDto.setUsername("UserB");
        userBDto.setPassword("1234");
        authService.signup(userBDto);
        userB = userRepository.findByEmail("userB@test.com").get();

        // 테스트용 공고 DTO 준비
        jobDto = new JobCreateRequestDto();
        jobDto.setCompanyName("Test Company");
        jobDto.setJobTitle("Backend Developer");
        jobDto.setDeadline(LocalDate.now().plusDays(10));
        jobDto.setJobType("신입");
    }

    @Test
    @DisplayName("공고 등록 성공 - 올바른 사용자와 연결되는지 확인")
    void createJob_success() {
        
        JobResponseDto responseDto = jobService.createJob(jobDto, userA.getEmail());

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getCompanyName()).isEqualTo("Test Company");

 
        Job savedJob = jobRepository.findById(responseDto.getId()).get();
        assertThat(savedJob.getUser().getId()).isEqualTo(userA.getId());
    }

    @Test
    @DisplayName("내 공고 목록 조회 - 본인 공고만 조회되는지 확인")
    void getMyJobs_success() {

        jobService.createJob(jobDto, userA.getEmail());

        List<JobResponseDto> userAJobs = jobService.getMyJobs(userA.getEmail());

        List<JobResponseDto> userBJobs = jobService.getMyJobs(userB.getEmail());

        
        assertThat(userAJobs).hasSize(1); // userA는 1개가 조회되어야 함
        assertThat(userAJobs.get(0).getCompanyName()).isEqualTo("Test Company");
        
        assertThat(userBJobs).isEmpty(); // userB는 0개가 조회되어야 함
    }

    @Test
    @DisplayName("공고 삭제 성공 - 본인 공고")
    void deleteJob_success() {

        // userA가 공고 생성
        JobResponseDto createdJobDto = jobService.createJob(jobDto, userA.getEmail());
        Long jobId = createdJobDto.getId();
        
        assertThat(jobRepository.existsById(jobId)).isTrue();

        // userA가 본인 공고 삭제 시도
        jobService.deleteJob(jobId, userA.getEmail());

        // (DB에서 삭제됐는지 최종 확인)
        assertThat(jobRepository.existsById(jobId)).isFalse();
    }

    @Test
    @DisplayName("공고 삭제 실패 (보안 테스트) - 타인의 공고")
    void deleteJob_fail_not_owner() {

        // userA가 공고 생성
        JobResponseDto createdJobDto = jobService.createJob(jobDto, userA.getEmail());
        Long jobIdOfUserA = createdJobDto.getId();


        // userB가 userA의 공고를 삭제하려고 시도
        assertThatThrownBy(() -> {
            jobService.deleteJob(jobIdOfUserA, userB.getEmail());
        })
        .isInstanceOf(AccessDeniedException.class)
        .hasMessageContaining("삭제할 권한이 없습니다.");

        assertThat(jobRepository.existsById(jobIdOfUserA)).isTrue();
    }

    @Test
    @DisplayName("공고 삭제 실패 - 존재하지 않는 공고")
    void deleteJob_fail_job_not_found() {

        Long nonExistentJobId = 9999L; // 존재하지 않는 ID

        // when & then
        assertThatThrownBy(() -> {
            jobService.deleteJob(nonExistentJobId, userA.getEmail());
        })
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("공고를 찾을 수 없습니다.");
    }
}