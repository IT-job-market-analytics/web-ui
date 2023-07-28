package com.itjobmarketanalytics.webui.controller;

import com.itjobmarketanalytics.webui.dto.SignUpDto;
import com.itjobmarketanalytics.webui.exception.RestApiException;
import com.itjobmarketanalytics.webui.service.RestApiClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class SignUpController {

    private final RestApiClientService service;

    public SignUpController(RestApiClientService service) {
        this.service = service;
    }


    @GetMapping("/sign-up")
    public String signUpView(Model model) {
        SignUpDto signUpDto = new SignUpDto();
        model.addAttribute("user", signUpDto);

        return "signup";
    }

    @PostMapping("/sign-up")
    public String submitSignUpForm(@ModelAttribute("user") SignUpDto signUpDto, Model model) {

        try {
            service.signUp(signUpDto);
        } catch (RestApiException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "signup";
        }

        return "redirect:/sign-in";
    }
}
