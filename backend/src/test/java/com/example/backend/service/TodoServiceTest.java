package com.example.backend.service;

import com.example.backend.domain.Todo;
import com.example.backend.domain.TodoCategory;
import com.example.backend.domain.User;
import com.example.backend.dto.SignupRequestDto;
import com.example.backend.dto.TodoCreateRequestDto;
import com.example.backend.dto.TodoResponseDto;
import com.example.backend.dto.TodoUpdateRequestDto;
import com.example.backend.repository.TodoRepository;
import com.example.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class TodoServiceTest {

    @Autowired
    private TodoService todoService;
    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;

    private User userA;
    private User userB;
    private TodoCreateRequestDto todoCreateDto;

    @BeforeEach
    void setUp() {
        
        SignupRequestDto userADto = new SignupRequestDto();
        userADto.setEmail("userA@todo.com");
        userADto.setUsername("UserA_Todo");
        userADto.setPassword("1234");
        authService.signup(userADto);
        userA = userRepository.findByEmail("userA@todo.com").get();

        
        SignupRequestDto userBDto = new SignupRequestDto();
        userBDto.setEmail("userB@todo.com");
        userBDto.setUsername("UserB_Todo");
        userBDto.setPassword("1234");
        authService.signup(userBDto);
        userB = userRepository.findByEmail("userB@todo.com").get();

        
        todoCreateDto = new TodoCreateRequestDto();
        todoCreateDto.setContent("오늘 할 일 1");
        todoCreateDto.setCategory(TodoCategory.TODAY);
    }

    @Test
    @DisplayName("Todo 생성 성공")
    void 할일_생성_성공_테스트() {
        
        TodoResponseDto responseDto = todoService.createTodo(todoCreateDto, userA.getEmail());
     
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getContent()).isEqualTo("오늘 할 일 1");
        assertThat(responseDto.getCategory()).isEqualTo(TodoCategory.TODAY);
        assertThat(responseDto.getIsDone()).isFalse();

        
        Todo savedTodo = todoRepository.findById(responseDto.getId()).get();
        assertThat(savedTodo.getUser().getId()).isEqualTo(userA.getId());
    }

    @Test
    @DisplayName("내 Todo 목록 조회")
    void 내_할일_목록_조회_테스트() {

        todoService.createTodo(todoCreateDto, userA.getEmail());
        
        TodoCreateRequestDto weekDto = new TodoCreateRequestDto();
        weekDto.setContent("이번 주 할 일 1");
        weekDto.setCategory(TodoCategory.WEEK);
        todoService.createTodo(weekDto, userA.getEmail());


        List<TodoResponseDto> userATodos = todoService.getMyTodos(userA.getEmail());
        List<TodoResponseDto> userBTodos = todoService.getMyTodos(userB.getEmail());

        assertThat(userATodos).hasSize(2); 
        assertThat(userBTodos).isEmpty(); 
    }

    @Test
    @DisplayName("Todo 상태 변경 (완료/미완료) 성공")
    void 할일_상태_변경_테스트() {
        
        TodoResponseDto createdDto = todoService.createTodo(todoCreateDto, userA.getEmail());
        Long todoId = createdDto.getId();

        
        TodoResponseDto updatedDto = todoService.updateTodoStatus(todoId, true, userA.getEmail());

        
        assertThat(updatedDto.getIsDone()).isTrue();
        

        Todo updatedTodo = todoRepository.findById(todoId).get();
        assertThat(updatedTodo.getIsDone()).isTrue();
    }
    
    @Test
    @DisplayName("Todo 상태 변경 (보안 테스트) - 타인의 Todo")
    void 할일_상태_변경_실패_타인_할일_테스트() {
        
        TodoResponseDto createdDto = todoService.createTodo(todoCreateDto, userA.getEmail());
        Long todoIdOfUserA = createdDto.getId();

        
        assertThatThrownBy(() -> {
            todoService.updateTodoStatus(todoIdOfUserA, true, userB.getEmail());
        })
        .isInstanceOf(AccessDeniedException.class)
        .hasMessageContaining("접근할 권한이 없습니다.");
    }

    @Test
    @DisplayName("Todo 내용 수정 성공")
    void 할일_내용_수정_테스트() {
        
        TodoResponseDto createdDto = todoService.createTodo(todoCreateDto, userA.getEmail());
        Long todoId = createdDto.getId();

        TodoUpdateRequestDto updateDto = new TodoUpdateRequestDto();
        updateDto.setContent("수정된 할 일 내용");

        
        TodoResponseDto updatedDto = todoService.updateTodoContent(todoId, updateDto, userA.getEmail());

        
        assertThat(updatedDto.getContent()).isEqualTo("수정된 할 일 내용");
        
        
        Todo updatedTodo = todoRepository.findById(todoId).get();
        assertThat(updatedTodo.getContent()).isEqualTo("수정된 할 일 내용");
    }

    @Test
    @DisplayName("Todo 삭제 성공")
    void 할일_삭제_테스트() {
        
        TodoResponseDto createdDto = todoService.createTodo(todoCreateDto, userA.getEmail());
        Long todoId = createdDto.getId();
        assertThat(todoRepository.existsById(todoId)).isTrue();

        
        todoService.deleteTodo(todoId, userA.getEmail());

        
        assertThat(todoRepository.existsById(todoId)).isFalse();
    }

    @Test
    @DisplayName("Todo 삭제 (보안 테스트) - 타인의 Todo")
    void 할일_삭제_실패_타인_할일_테스트() {
        
        TodoResponseDto createdDto = todoService.createTodo(todoCreateDto, userA.getEmail());
        Long todoIdOfUserA = createdDto.getId();

        
        assertThatThrownBy(() -> {
            todoService.deleteTodo(todoIdOfUserA, userB.getEmail());
        })
        .isInstanceOf(AccessDeniedException.class)
        .hasMessageContaining("접근할 권한이 없습니다.");

        
        assertThat(todoRepository.existsById(todoIdOfUserA)).isTrue();
    }
}