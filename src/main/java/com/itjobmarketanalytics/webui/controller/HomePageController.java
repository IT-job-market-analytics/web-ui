package com.itjobmarketanalytics.webui.controller;

import com.itjobmarketanalytics.webui.dto.UserDto;
import com.itjobmarketanalytics.webui.exception.RestApiException;
import com.itjobmarketanalytics.webui.service.RestApiClientService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomePageController {

    private final RestApiClientService service;

    public HomePageController(RestApiClientService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String homeView(Model model, HttpSession session) {

        try {
            UserDto userDto = service.getUser((String) session.getAttribute("accessToken"));
            model.addAttribute("username", userDto.getUsername());
            model.addAttribute("isLoggedIn", true);
        } catch (RestApiException e) {
            String errorMessage = e.getMessage();
            if (!errorMessage.equalsIgnoreCase("Unauthorized")) {
                model.addAttribute("errorMessage", e.getMessage());
            }
        }

        return "index";
    }


}
