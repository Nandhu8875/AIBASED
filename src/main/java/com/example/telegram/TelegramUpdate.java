package com.example.telegram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Root object received from Telegram Webhook.
 * Example payload:
 * {
 * "update_id": 123456789,
 * "message": {
 * "message_id": 1,
 * "from": { "id": 123456789, "first_name": "John" },
 * "chat": { "id": 123456789 },
 * "text": "Math 2.5"
 * }
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramUpdate {

    @JsonProperty("update_id")
    private Long updateId;

    @JsonProperty("message")
    private TelegramMessage message;

    public Long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public TelegramMessage getMessage() {
        return message;
    }

    public void setMessage(TelegramMessage message) {
        this.message = message;
    }
}
