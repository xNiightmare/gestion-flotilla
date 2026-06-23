package com.grandedev.gestionflotilla.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TelegramMessage(@JsonProperty("chat_id") Long chatId,
                              String text) {
}
