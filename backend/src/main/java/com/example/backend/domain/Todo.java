package com.example.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean isDone = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    
    public Todo(String content, TodoCategory category, User user) {
        this.content = content;
        this.category = category;
        this.user = user;
    }


    public void updateStatus(Boolean isDone) {
        this.isDone = isDone;
    }
    
    public void updateContent(String content) {
        this.content = content;
    }
}