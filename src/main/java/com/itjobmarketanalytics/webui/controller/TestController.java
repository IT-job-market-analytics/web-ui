package com.itjobmarketanalytics.webui.controller;

import com.itjobmarketanalytics.webui.dto.SignInDto;
import com.itjobmarketanalytics.webui.dto.SignInResponseDto;
import com.itjobmarketanalytics.webui.dto.SignUpDto;
import com.itjobmarketanalytics.webui.dto.UserDto;
import com.itjobmarketanalytics.webui.exception.RestApiException;
import com.itjobmarketanalytics.webui.service.RestApiClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestParam String password) throws RestApiException {
        return restApiClientService.signIn(new SignInDto(username, password));
    }

    @GetMapping(value = {"/sign-up"})
    public void signUp(
            @RequestParam String username,
            @RequestParam String password) throws RestApiException {
        restApiClientService.signUp(new SignUpDto(username, password));
    }

    @GetMapping(value = {"/user"})
    public UserDto getUser(@RequestParam String token) throws RestApiException {
        return restApiClientService.getUser(token);
    }
}
