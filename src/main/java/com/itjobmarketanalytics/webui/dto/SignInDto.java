package com.itjobmarketanalytics.webui.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class SignInDto {
    String username;
    String password;
}
