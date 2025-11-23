package com.example.backend.service;

import com.example.backend.domain.Todo;
import com.example.backend.domain.User;
import com.example.backend.dto.TodoCreateRequestDto;
import com.example.backend.dto.TodoResponseDto;
import com.example.backend.dto.TodoUpdateRequestDto;
import com.example.backend.repository.TodoRepository;
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
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    private Todo findTodoByIdAndUser(Long todoId, User user) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("Todo를 찾을 수 없습니다."));

        if (!todo.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("이 Todo에 접근할 권한이 없습니다.");
        }
        return todo;
    }

    public List<TodoResponseDto> getMyTodos(String userEmail) {
        User user = getUserByEmail(userEmail);
        List<Todo> todos = todoRepository.findAllByUser(user); // JPA 쿼리 메소드 사용

        return todos.stream()
                .map(TodoResponseDto::new)
                .collect(Collectors.toList());
    }


    @Transactional
    public TodoResponseDto createTodo(TodoCreateRequestDto dto, String userEmail) {
        User user = getUserByEmail(userEmail);
        Todo todo = new Todo(dto.getContent(), dto.getCategory(), user);
        Todo savedTodo = todoRepository.save(todo);
        return new TodoResponseDto(savedTodo);
    }


    @Transactional
    public TodoResponseDto updateTodoStatus(Long todoId, Boolean isDone, String userEmail) {
        User user = getUserByEmail(userEmail);
        Todo todo = findTodoByIdAndUser(todoId, user); 

        todo.updateStatus(isDone); 
        return new TodoResponseDto(todo);
    }


    @Transactional
    public TodoResponseDto updateTodoContent(Long todoId, TodoUpdateRequestDto dto, String userEmail) {
        User user = getUserByEmail(userEmail);
        Todo todo = findTodoByIdAndUser(todoId, user);

        todo.updateContent(dto.getContent());

        return new TodoResponseDto(todo);
    }

  
    @Transactional
    public void deleteTodo(Long todoId, String userEmail) {
        User user = getUserByEmail(userEmail);
        Todo todo = findTodoByIdAndUser(todoId, user); // 소유권 검증 포함

        todoRepository.delete(todo);
    }
}