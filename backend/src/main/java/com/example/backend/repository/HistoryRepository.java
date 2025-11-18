package com.example.backend.repository;

import com.example.backend.domain.History;
import com.example.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findAllByUserOrderByCreatedAtDesc(User user);

}