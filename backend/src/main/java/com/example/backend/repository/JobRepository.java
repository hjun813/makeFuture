package com.example.backend.repository;

import com.example.backend.domain.Job;
import com.example.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findAllByUserOrderByDeadlineAsc(User user); 
    Optional<Job> findByIdAndUser(Long id, User user);
    List<Job> findAllByOrderByDeadlineAsc();
}