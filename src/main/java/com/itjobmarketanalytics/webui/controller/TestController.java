package com.itjobmarketanalytics.webui.controller;

import com.itjobmarketanalytics.webui.dto.SignInDto;
import com.itjobmarketanalytics.webui.dto.SignInResponseDto;
import com.itjobmarketanalytics.webui.dto.SignUpDto;
import com.itjobmarketanalytics.webui.dto.UserDto;
import com.itjobmarketanalytics.webui.exception.RestApiException;
import com.itjobmarketanalytics.webui.service.RestApiClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    private final RestApiClientService restApiClientService;

    public TestController(RestApiClientService restApiClientService) {
        this.restApiClientService = restApiClientService;
    }

    @GetMapping(value = {"/sign-in"})
    public SignInResponseDto signIn(
            @RequestParam String username,
            @RequestParam String password
    ) throws RestApiException {
        return restApiClientService.signIn(new SignInDto(username, password));
    }

    @GetMapping(value = {"/sign-up"})
    public void signUp(
            @RequestParam String username,
            @RequestParam String password
    ) throws RestApiException {
        restApiClientService.signUp(new SignUpDto(username, password));
    }

    @GetMapping(value = {"/user"})
    public UserDto getUser(@RequestParam String token) throws RestApiException {
        return restApiClientService.getUser(token);
    }

    @GetMapping(value = {"/profile"})
    public void updateUser(
            @RequestParam Long telegramChatId,
            @RequestParam String token
    ) throws RestApiException {
        log.info("Test: updateUser -> start");
        restApiClientService.updateUser(telegramChatId, token);
        log.info("Test: updateUser -> success");
    }

    @GetMapping(value = {"/subscriptions"})
    public void getCurrentSubscription(@RequestParam String token) throws RestApiException {
        log.info("Test: get subscriptions -> start");
        restApiClientService.getCurrentSubscriptions(token);
        log.info("Test: get subscriptions -> success");
    }

    @GetMapping(value = {"/subscriptions/allAvailable"})
    public void getAllAvailableSubscriptions(@RequestParam String token) throws RestApiException {
        log.info("Test: get all available subscriptions -> start");
        restApiClientService.getAvailableSubscriptions(token);
        log.info("Test: get all available subscriptions -> success");
    }

    @GetMapping(value = {"/subscriptions/add"})
    public void addSubscriptions(
            @RequestParam String query,
            @RequestParam String token
            ) throws RestApiException {
        log.info("Test: add subscriptions -> start");
        restApiClientService.addSubscriptions(query, token);
        log.info("Test: add subscriptions -> success");
    }

    @GetMapping(value = {"/subscriptions/delete"})
    public void deleteSubscriptions(
            @RequestParam String query,
            @RequestParam String token
    ) throws RestApiException {
        log.info("Test: delete subscriptions -> start");
        restApiClientService.removeSubscriptions(query, token);
        log.info("Test: delete subscriptions -> success");
    }
}
