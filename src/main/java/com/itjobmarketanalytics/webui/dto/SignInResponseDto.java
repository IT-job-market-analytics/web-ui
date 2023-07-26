package com.itjobmarketanalytics.webui.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class SignInResponseDto {
    Long id;
    String username;
    String accessToken;
    String refreshToken;
}
