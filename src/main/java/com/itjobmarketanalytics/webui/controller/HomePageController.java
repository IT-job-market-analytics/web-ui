package com.itjobmarketanalytics.webui.controller;

import com.itjobmarketanalytics.webui.dto.UserDto;
import com.itjobmarketanalytics.webui.exception.RestApiException;
import com.itjobmarketanalytics.webui.service.RestApiClientService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.HttpServerErrorException;

@Controller
@Slf4j
public class HomePageController {

    private final RestApiClientService service;

    public HomePageController(RestApiClientService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String homeView(Model model, HttpSession session) {
        log.info("Access token -> {}", session.getAttribute("accessToken"));

        try {
            UserDto userDto = service.getUser((String) session.getAttribute("accessToken"));
            log.info("Username -> {}", userDto.getUsername());
            model.addAttribute("username", userDto.getUsername());
        } catch (RestApiException e) {
            log.info("I can catch RestApiException");
        } catch (HttpServerErrorException e) {
            log.info("I can catch error 500");
        }

        return "index";
    }


}
