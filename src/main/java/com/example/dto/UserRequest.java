package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserRequest {

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotNull(message = "Telegram ID must not be null")
    private Long telegramId;

    // Constructors
    public UserRequest() {
    }

    public UserRequest(String name, Long telegramId) {
        this.name = name;
        this.telegramId = telegramId;
    }

    // Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
    }
}
