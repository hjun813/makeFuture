package com.example.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String jobTitle;

    private String url;

    private LocalDate deadline;

    private String jobType;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "user_id", nullable = false) 
    private User user;


    public Job(String companyName, String jobTitle, String url, LocalDate deadline, String jobType, User user) {
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.url = url;
        this.deadline = deadline;
        this.jobType = jobType;
        this.user = user;
    }

    public void update(String companyName, String jobTitle, String url, LocalDate deadline, String jobType) {
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.url = url;
        this.deadline = deadline;
        this.jobType = jobType;
    }
}