package com.itjobmarketanalytics.webui.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.itjobmarketanalytics.webui.dto.SignInDto;
import com.itjobmarketanalytics.webui.dto.SignInResponseDto;
import com.itjobmarketanalytics.webui.dto.SignUpDto;
import com.itjobmarketanalytics.webui.dto.UserDto;
import com.itjobmarketanalytics.webui.exception.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class RestApiClientService {

    RestTemplate restTemplate;

    public RestApiClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${app.hostname}")
    String host;

    private final String SING_IN = host + "/auth/signin";
    private final String SING_UP = host + "/auth/signup";
    private final String USER = host + "/user/";

    public void signUp(SignUpDto dto) throws RestApiException {

        try {
            log.info("Start request for signUp to {}", SING_UP);
            restTemplate.postForEntity(SING_UP, dto, String.class);
            log.info("SignUp request DONE");

        } catch (HttpClientErrorException e) {

            int status = e.getStatusCode().value();
            String message = e.getResponseBodyAsString();
            String messageResponse = null;
            try {
                messageResponse = new Gson().fromJson(message, JsonObject.class).get("message").getAsString();
            } catch (JsonSyntaxException ex) {
                messageResponse = "Something is wrong, try again later";
            }

            log.info("Response status code -> {}", status);
            log.info("Exception message -> {}", messageResponse);

            throw new RestApiException(messageResponse);
        }
    }

    public SignInResponseDto signIn(SignInDto dto) throws RestApiException {
        SignInResponseDto signInResponseDto;
        try {
            HttpEntity<SignInDto> request = new HttpEntity<>(dto);
            log.info("Start request for signIn to {}", SING_IN);

            String jsonResponseDto = restTemplate.exchange(SING_IN, HttpMethod.POST, request, String.class).getBody();
            signInResponseDto = new Gson().fromJson(jsonResponseDto, SignInResponseDto.class);

            log.info("SignIn request DONE");
            log.info("UserDto -> {}", signInResponseDto);

        } catch (HttpClientErrorException e) {

            int status = e.getStatusCode().value();
            String message = e.getResponseBodyAsString();
            String messageResponse = null;
            try {
                messageResponse = new Gson().fromJson(message, JsonObject.class).get("message").getAsString();
            } catch (JsonSyntaxException ex) {
                messageResponse = "Something is wrong, try again later";
            }

            log.info("Response status code -> {}", status);
            log.info("Exception message -> {}", messageResponse);

            throw new RestApiException(messageResponse);
        }
        return signInResponseDto;
    }

    public UserDto getUser(String token) throws RestApiException {
        String responseUserDto = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            responseUserDto = restTemplate.exchange(USER, HttpMethod.GET, entity, String.class).getBody();
            log.info("Get user by Auth -> {}", responseUserDto);

        } catch (HttpClientErrorException e) {
            int status = e.getStatusCode().value();
            String message = e.getResponseBodyAsString();
            String messageResponse = null;
            try {
                messageResponse = new Gson().fromJson(message, JsonObject.class).get("message").getAsString();
            } catch (JsonSyntaxException ex) {
                messageResponse = "Something is wrong, try again later";
            }

            log.info("Response status code -> {}", status);
            log.info("Exception message -> {}", messageResponse);
            throw new RestApiException(messageResponse);
        }

        UserDto userDto = null;
        try {
            userDto = new Gson().fromJson(responseUserDto, UserDto.class);
            log.info("Get userDto object -> {}", userDto);
        } catch (JsonSyntaxException e) {
            String messageResponse = "Something is wrong, try again later";
            throw new RestApiException(messageResponse);

        }
        return userDto;
    }
}
