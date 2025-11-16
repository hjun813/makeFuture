package com.example.backend.controller;

import com.example.backend.dto.TodoCreateRequestDto;
import com.example.backend.dto.TodoResponseDto;
import com.example.backend.dto.TodoUpdateRequestDto;
import com.example.backend.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos") 
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public ResponseEntity<List<TodoResponseDto>> getMyTodos(Authentication authentication) {
        String email = authentication.getName();
        List<TodoResponseDto> todos = todoService.getMyTodos(email);
        return ResponseEntity.ok(todos);
    }

    @PostMapping
    public ResponseEntity<TodoResponseDto> createTodo(@Valid @RequestBody TodoCreateRequestDto dto,
                                                      Authentication authentication) {
        String email = authentication.getName();
        TodoResponseDto createdTodo = todoService.createTodo(dto, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }


    @PatchMapping("/{todoId}/status")
    public ResponseEntity<TodoResponseDto> updateTodoStatus(@PathVariable Long todoId,
                                                            @RequestBody Map<String, Boolean> payload,
                                                            Authentication authentication) {
        String email = authentication.getName();
        Boolean isDone = payload.get("isDone");
        
        TodoResponseDto updatedTodo = todoService.updateTodoStatus(todoId, isDone, email);
        return ResponseEntity.ok(updatedTodo);
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<TodoResponseDto> updateTodoContent(@PathVariable Long todoId,
                                                             @Valid @RequestBody TodoUpdateRequestDto dto,
                                                             Authentication authentication) {
        String email = authentication.getName();
        TodoResponseDto updatedTodo = todoService.updateTodoContent(todoId, dto, email);
        return ResponseEntity.ok(updatedTodo);
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long todoId,
                                           Authentication authentication) {
        String email = authentication.getName();
        todoService.deleteTodo(todoId, email);
        return ResponseEntity.noContent().build();
    }
}