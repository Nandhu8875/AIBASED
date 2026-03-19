package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class StudyLogRequest {

    @NotBlank(message = "Subject must not be blank")
    private String subject;

    @NotNull(message = "Hours must not be null")
    @Positive(message = "Hours must be a positive number")
    private Double hours;

    @NotNull(message = "Date must not be null")
    private LocalDate date;

    // Constructors
    public StudyLogRequest() {
    }

    public StudyLogRequest(String subject, Double hours, LocalDate date) {
        this.subject = subject;
        this.hours = hours;
        this.date = date;
    }

    // Getters & Setters
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
}
