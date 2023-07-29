package com.itjobmarketanalytics.webui.controller;

import com.itjobmarketanalytics.webui.dto.UserDto;
import com.itjobmarketanalytics.webui.exception.RestApiException;
import com.itjobmarketanalytics.webui.exception.RestApiUnauthorizedException;
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
        boolean isLoggedIn = true;
        try {
            UserDto userDto = service.getUser((String) session.getAttribute("accessToken"));
            model.addAttribute("username", userDto.getUsername());
        } catch (RestApiException e) {
            if (!(e instanceof RestApiUnauthorizedException)) {
                model.addAttribute("errorMessage", e.getMessage());
            }

            isLoggedIn = false;
        }
        model.addAttribute("isLoggedIn", isLoggedIn);

        return "index";
    }


}
