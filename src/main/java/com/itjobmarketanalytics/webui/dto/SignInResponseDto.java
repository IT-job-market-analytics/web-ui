package com.itjobmarketanalytics.webui.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponseDto {
    Long id;
    String username;
    String accessToken;
    String refreshToken;
}
