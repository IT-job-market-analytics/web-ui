package com.itjobmarketanalytics.webui.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

    private static final Gson gson = new Gson();

    @Value("${app.hostname}")
    String host;

    private static final String SING_IN = "/auth/signin";
    private static final String SING_UP = "/auth/signup";
    private static final String GET_USER = "/user/";

    public void signUp(SignUpDto dto) throws RestApiException {
        String url = host + SING_UP;

        try {
            restTemplate.postForEntity(url, dto, String.class);
            log.debug("Sign up request successfully executed");
        } catch (HttpClientErrorException e) {
            throw convertException(e);
        }
    }

    public SignInResponseDto signIn(SignInDto dto) throws RestApiException {
        String url = host + SING_IN;

        try {
            HttpEntity<SignInDto> request = new HttpEntity<>(dto);
            ResponseEntity<SignInResponseDto> response = restTemplate.exchange(
                    url, HttpMethod.POST, request, SignInResponseDto.class
            );

            log.debug("Sign in request successfully executed");

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw convertException(e);
        }
    }

    public UserDto getUser(String token) throws RestApiException {
        String url = host + GET_USER;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null) {
            headers.set("Authorization", "Bearer " + token);
        }
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<UserDto> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, UserDto.class);
            return responseEntity.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw convertException(e);
        }
    }

    private RestApiException convertException(Exception e) {
        if (e instanceof HttpServerErrorException) {
            return new RestApiUnknownException("Unknown error");
        }

        if (!(e instanceof HttpClientErrorException clientException)) {
            return new RestApiUnknownException("Unknown error");
        }

        if (clientException instanceof HttpClientErrorException.Unauthorized) {
            String exceptionMessage = extractErrorMessage(clientException, "Unauthorized");

            return new RestApiUnauthorizedException(exceptionMessage);
        } else if (clientException.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            return new RestApiUnknownException("Unknown error");
        }

        String exceptionMessage = extractErrorMessage(clientException, "Unknown error");
        log.info("Exception message -> {} ; status code -> {}", exceptionMessage, clientException.getStatusCode());

        return new RestApiException(exceptionMessage);
    }

    private String getResponseMessageValue(String responseMessage) {
        return gson.fromJson(responseMessage, JsonObject.class).get("message").getAsString();
    }

    private String extractErrorMessage(HttpClientErrorException exception, String defaultMessage) {
        String responseMessage = exception.getResponseBodyAsString();
        try {
            return getResponseMessageValue(responseMessage);
        } catch (Exception ex) {
            return defaultMessage;
        }
    }
}
