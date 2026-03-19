package com.example.dto;

import java.time.LocalDate;

public class StudyLogResponse {

    private Long id;
    private String subject;
    private Double hours;
    private LocalDate date;
    private Long userId;
    private String userName;

    // Constructors
    public StudyLogResponse() {
    }

    public StudyLogResponse(Long id, String subject, Double hours, LocalDate date,
            Long userId, String userName) {
        this.id = id;
        this.subject = subject;
        this.hours = hours;
        this.date = date;
        this.userId = userId;
        this.userName = userName;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
