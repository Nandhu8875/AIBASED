package com.example.service;

import com.example.entity.User;
import com.example.exception.DuplicateTelegramIdException;
import com.example.exception.UserNotFoundException;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user.
     * Throws DuplicateTelegramIdException if the telegramId is already registered.
     */
    @Transactional
    public User createUser(String name, Long telegramId) {
        String telegramIdStr = String.valueOf(telegramId);

        if (userRepository.existsByTelegramId(telegramIdStr)) {
            throw new DuplicateTelegramIdException(telegramId);
        }

        User user = new User();
        user.setName(name);
        user.setTelegramId(telegramIdStr);
        return userRepository.save(user);
    }

    /**
     * Fetches a user by their Telegram ID.
     * Throws UserNotFoundException if no user is found.
     */
    @Transactional(readOnly = true)
    public User getUserByTelegramId(Long telegramId) {
        String telegramIdStr = String.valueOf(telegramId);
        return userRepository.findByTelegramId(telegramIdStr)
                .orElseThrow(() -> new UserNotFoundException(telegramId));
    }
}
