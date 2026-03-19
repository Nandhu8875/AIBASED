package com.example.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long telegramId) {
        super("User not found with Telegram ID: " + telegramId);
    }
}
