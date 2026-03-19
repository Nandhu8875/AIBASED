package com.example.controller;

import com.example.service.TelegramService;
import com.example.telegram.TelegramUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/telegram")
@Tag(name = "Telegram Bot", description = "Webhook endpoint for Telegram Bot integration. Called by Telegram servers — no JWT required.")
public class TelegramController {

    private static final Logger log = LoggerFactory.getLogger(TelegramController.class);

    private final TelegramService telegramService;

    public TelegramController(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @Operation(summary = "Telegram Webhook", description = """
            Receives updates from Telegram Bot API.
            **Not for manual use** — called automatically by Telegram.

            Message format users should send to bot:
            ```
            Math 2.5
            Physics 1.0
            Linear Algebra 3.0
            ```
            Format: `<subject> <hours>`
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Update processed successfully")
    })
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody TelegramUpdate update) {
        log.info("Webhook received: update_id={}", update.getUpdateId());
        try {
            telegramService.processUpdate(update);
        } catch (Exception e) {
            log.error("Error processing Telegram update: {}", e.getMessage());
        }
        return ResponseEntity.ok("OK");
    }
}
