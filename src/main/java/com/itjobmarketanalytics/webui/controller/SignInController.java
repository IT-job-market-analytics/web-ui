package com.itjobmarketanalytics.webui.controller;

import com.itjobmarketanalytics.webui.dto.SignInDto;
import com.itjobmarketanalytics.webui.dto.SignInResponseDto;
import com.itjobmarketanalytics.webui.exception.RestApiException;
import com.itjobmarketanalytics.webui.service.RestApiClientService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class SignInController {

    private final RestApiClientService service;

    public SignInController(RestApiClientService service) {
        this.service = service;
    }

    @GetMapping("/sign-in")
    public String signInView(Model model) {
        SignInDto signInDto = new SignInDto();
        model.addAttribute("user", signInDto);

        return "signin";
    }

    @PostMapping("/sign-in")
    public String submitSignInForm(@ModelAttribute("user") SignInDto signInDto, Model model, HttpSession session) {

        try {
            SignInResponseDto dto = service.signIn(signInDto);
            session.setAttribute("accessToken", dto.getAccessToken());
        } catch (RestApiException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "signin";
        }

        return "redirect:/";
    }
}
