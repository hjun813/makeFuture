package com.example.backend.repository;

import com.example.backend.domain.Todo;
import com.example.backend.domain.TodoCategory;
import com.example.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findAllByUser(User user);

    List<Todo> findAllByUserAndCategory(User user, TodoCategory category);
}