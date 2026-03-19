package com.example.controller;

import com.example.dto.UserRequest;
import com.example.entity.User;
import com.example.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /api/users/register
     * Body (JSON): { "name": "John", "telegramId": 123456789 }
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRequest request) {
        User user = userService.createUser(request.getName(), request.getTelegramId());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * GET /api/users/{telegramId}
     */
    @GetMapping("/{telegramId}")
    public ResponseEntity<User> getUserByTelegramId(@PathVariable Long telegramId) {
        User user = userService.getUserByTelegramId(telegramId);
        return ResponseEntity.ok(user);
    }
}
