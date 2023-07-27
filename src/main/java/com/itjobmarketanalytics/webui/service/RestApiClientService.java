package com.itjobmarketanalytics.webui.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.itjobmarketanalytics.webui.dto.SignInDto;
import com.itjobmarketanalytics.webui.dto.SignInResponseDto;
import com.itjobmarketanalytics.webui.dto.SignUpDto;
import com.itjobmarketanalytics.webui.dto.UserDto;
import com.itjobmarketanalytics.webui.exception.RestApiException;
import com.itjobmarketanalytics.webui.exception.RestApiUnauthorizedException;
import com.itjobmarketanalytics.webui.exception.RestApiUnknownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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

    private final String SING_IN = "/auth/signin";
    private final String SING_UP = "/auth/signup";
    private final String GET_USER = "/user/";

    public void signUp(SignUpDto dto) throws RestApiException {
        String url = host + SING_UP;
        log.info("Start request for signUp to {}", url);

        try {
            restTemplate.postForEntity(url, dto, String.class);
            log.info("SignUp request DONE");
        } catch (HttpClientErrorException e) {
            handleException(e);
        }
    }

    public SignInResponseDto signIn(SignInDto dto) throws RestApiException {
        SignInResponseDto signInResponseDto = null;
        String url = host + SING_IN;
        log.info("Start request for signIn to {}", url);

        try {
            HttpEntity<SignInDto> request = new HttpEntity<>(dto);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            signInResponseDto = new Gson().fromJson(response.getBody(), SignInResponseDto.class);
            log.info("SignIn request DONE");
        } catch (HttpClientErrorException e) {
            handleException(e);
        }
        return signInResponseDto;
    }

    public UserDto getUser(String token) throws RestApiException {
        String url = host + GET_USER;
        log.info("Start request for getUser to {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String responseUserDto = null;
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            responseUserDto = responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            handleException(e);
        } catch (HttpServerErrorException e) {
            throw new RestApiUnknownException("Unknown error");
        }

        UserDto userDto;
        try {
            userDto = new Gson().fromJson(responseUserDto, UserDto.class);
        } catch (JsonSyntaxException e) {
            throw new RestApiUnknownException("Unknown error");
        }
        return userDto;
    }

    private void handleException(HttpClientErrorException e) throws RestApiException {

        if (isUnauthorized(e)) {
            throw new RestApiUnauthorizedException("Wrong password");
        }

        if (isUnknown(e)) {
            throw new RestApiUnknownException("Unknown error");
        }

        String responseMessage = e.getResponseBodyAsString();
        String exceptionMessage;
        try {
            exceptionMessage = getResponseMessageValue(responseMessage);
        } catch (JsonSyntaxException ex) {
            exceptionMessage = "Unknown error";
        }
        log.info("Exception message -> {} ; status code -> {}", exceptionMessage, e.getStatusCode());

        throw new RestApiException(exceptionMessage);
    }

    private String getResponseMessageValue(String responseMessage) {
        return new Gson().fromJson(responseMessage, JsonObject.class).get("message").getAsString();
    }

    private boolean isUnauthorized(HttpClientErrorException e) {
        return e.getStatusCode().equals(HttpStatus.UNAUTHORIZED);
    }

    private boolean isUnknown(HttpClientErrorException e) {
        return e.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
