package com.example.exception;

public class DuplicateTelegramIdException extends RuntimeException {

    public DuplicateTelegramIdException(Long telegramId) {
        super("User already exists with Telegram ID: " + telegramId);
    }

    public DuplicateTelegramIdException(String message) {
        super(message);
    }
}
