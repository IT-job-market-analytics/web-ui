package com.itjobmarketanalytics.webui.dto;

import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {
    @Max(value = Long.MAX_VALUE, message = "Telegram chat id must be a valid number")
    Long telegramChatId;
}
