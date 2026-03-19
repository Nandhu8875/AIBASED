package com.example.service;

import com.example.entity.StudyLog;
import com.example.entity.User;
import com.example.repository.StudyLogRepository;
import com.example.repository.UserRepository;
import com.example.telegram.TelegramUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TelegramService {

    private static final Logger log = LoggerFactory.getLogger(TelegramService.class);

    private final UserRepository userRepository;
    private final StudyLogRepository studyLogRepository;
    private final RestTemplate restTemplate;

    @Value("${telegram.bot.token}")
    private String botToken;

    public TelegramService(UserRepository userRepository,
            StudyLogRepository studyLogRepository) {
        this.userRepository = userRepository;
        this.studyLogRepository = studyLogRepository;
        this.restTemplate = new RestTemplate();
    }

    /**
     * Main entry point — processes every incoming Telegram update.
     */
    public void processUpdate(TelegramUpdate update) {
        if (update.getMessage() == null || update.getMessage().getText() == null) {
            log.warn("Received update with no message or text. Skipping.");
            return;
        }

        Long chatId = update.getMessage().getChat().getId();
        Long telegramId = update.getMessage().getFrom().getId();
        String text = update.getMessage().getText().trim();

        log.info("Received message from telegramId={}: '{}'", telegramId, text);

        // ── Handle /start command ───────────────────────────────────────────
        if (text.startsWith("/start")) {
            sendMessage(chatId,
                    "👋 Welcome to AI Study Tracker!\n\n" +
                            "📌 First register at:\n" +
                            "POST /api/auth/register\n\n" +
                            "📝 Then log your study hours like this:\n" +
                            "  Math 2.5\n" +
                            "  Physics 1.0\n\n" +
                            "Format: <subject> <hours>");
            return;
        }

        // ── Check if user is registered ────────────────────────────────────
        Optional<User> userOptional = userRepository.findByTelegramId(String.valueOf(telegramId));
        if (userOptional.isEmpty()) {
            sendMessage(chatId,
                    "❌ You are not registered yet!\n\n" +
                            "Please register first using:\n" +
                            "POST /api/auth/register\n" +
                            "Include your Telegram ID: " + telegramId);
            return;
        }

        User user = userOptional.get();

        // ── Parse "Subject Hours" format ───────────────────────────────────
        String[] parts = text.split("\\s+");
        if (parts.length < 2) {
            sendMessage(chatId,
                    "⚠️ Invalid format!\n\n" +
                            "Please use:\n  <subject> <hours>\n\n" +
                            "Example:\n  Math 2.5\n  Physics 1.0");
            return;
        }

        double hours;
        try {
            hours = Double.parseDouble(parts[parts.length - 1]);
            if (hours <= 0)
                throw new NumberFormatException("Hours must be positive");
        } catch (NumberFormatException e) {
            sendMessage(chatId,
                    "⚠️ Invalid hours value!\n\n" +
                            "Hours must be a positive number.\n" +
                            "Example: Math 2.5");
            return;
        }

        // Subject can be multi-word (e.g. "Linear Algebra 2.0")
        String subject = String.join(" ",
                java.util.Arrays.copyOf(parts, parts.length - 1));

        // ── Save to DB ─────────────────────────────────────────────────────
        try {
            StudyLog studyLog = new StudyLog();
            studyLog.setUser(user);
            studyLog.setSubject(subject);
            studyLog.setHours(hours);
            studyLog.setDate(LocalDate.now());
            studyLogRepository.save(studyLog);

            log.info("Saved study log for user={} subject='{}' hours={}", user.getUsername(), subject, hours);

            sendMessage(chatId,
                    "✅ Study log saved!\n\n" +
                            "📚 Subject: " + subject + "\n" +
                            "⏱ Hours: " + hours + "\n" +
                            "📅 Date: " + LocalDate.now());

        } catch (Exception e) {
            log.error("Failed to save study log for telegramId={}: {}", telegramId, e.getMessage());
            sendMessage(chatId, "❌ Failed to save study log. Please try again.");
        }
    }

    /**
     * Sends a text message to a Telegram chat via Bot API.
     */
    public void sendMessage(Long chatId, String text) {
        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

        Map<String, Object> body = new HashMap<>();
        body.put("chat_id", chatId);
        body.put("text", text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForObject(url, request, String.class);
            log.info("Message sent to chatId={}", chatId);
        } catch (Exception e) {
            log.error("Failed to send Telegram message to chatId={}: {}", chatId, e.getMessage());
        }
    }
}
