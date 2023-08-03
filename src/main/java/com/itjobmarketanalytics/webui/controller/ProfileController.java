package com.itjobmarketanalytics.webui.controller;

import com.itjobmarketanalytics.webui.dto.UserDto;
import com.itjobmarketanalytics.webui.dto.UserSubscriptionsDto;
import com.itjobmarketanalytics.webui.dto.UserUpdateDto;
import com.itjobmarketanalytics.webui.exception.RestApiException;
import com.itjobmarketanalytics.webui.exception.RestApiUnauthorizedException;
import com.itjobmarketanalytics.webui.service.RestApiClientService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
public class ProfileController {

    private final RestApiClientService service;

    public ProfileController(RestApiClientService service) {
        this.service = service;
    }

    @GetMapping("/profile")
    public String profileView(Model model, HttpSession session) {
        boolean isLoggedIn = true;
        String token = (String) session.getAttribute("accessToken");
        try {
            UserDto userDto = service.getUser((String) session.getAttribute("accessToken"));

            UserDto user = service.getUser(token);
            model.addAttribute("username", userDto.getUsername());
            model.addAttribute("updateUser", new UserUpdateDto(user.getTelegramChatId()));

            List<UserSubscriptionsDto> availableSubscriptions = service.getAvailableSubscriptions(token);
            model.addAttribute("availableSubscriptions", availableSubscriptions);
            model.addAttribute("addSubscription", new UserSubscriptionsDto());

            List<UserSubscriptionsDto> currentSubscriptions = service.getCurrentSubscriptions(token);
            model.addAttribute("currentSubscriptions", currentSubscriptions);
            model.addAttribute("removeSubscription", new UserSubscriptionsDto());
        } catch (RestApiException e) {
            if (e instanceof RestApiUnauthorizedException) {
                return "redirect:/sign-in";
            }
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("isLoggedIn", isLoggedIn);

        return "profile";
    }

    @PostMapping("/profile/editUser")
    public String handleUserUpdate(
            @Valid @ModelAttribute("updateUser")
            UserUpdateDto userUpdateDto,
            BindingResult errors,

            HttpSession session, RedirectAttributes redirectAttributes
    ) {
        if (errors.hasErrors()) {
            ObjectError objectError = errors.getAllErrors().get(0);

            redirectAttributes.addFlashAttribute("updateMessage", objectError.getDefaultMessage());
            redirectAttributes.addFlashAttribute("updateAlertClass", "alert-danger");
            return "redirect:/profile";
        }

        try {
            service.updateUser(userUpdateDto.getTelegramChatId(), (String)session.getAttribute("accessToken"));
            redirectAttributes.addFlashAttribute("updateMessage", "Successfully updated");
            redirectAttributes.addFlashAttribute("updateAlertClass", "alert-success");
        } catch (RestApiException e) {
            if (e instanceof RestApiUnauthorizedException) {
                return "redirect:/sign-in";
            }
            redirectAttributes.addFlashAttribute("updateMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("updateAlertClass", "alert-danger");
            return "redirect:/profile";
        }

        return "redirect:/profile";
    }

    @PostMapping("/profile/addSubscription")
    public String addSubscription(
            @ModelAttribute("addSubscription")  UserSubscriptionsDto addSubscription,
                                                HttpSession session,
                                                RedirectAttributes redirectAttributes) {
        try {
            service.addSubscriptions(addSubscription.getQuery(), (String) session.getAttribute("accessToken"));
            redirectAttributes.addFlashAttribute("addMessage", "Successfully added");
            redirectAttributes.addFlashAttribute("addAlertClass", "alert-success");
        } catch (RestApiException e) {
            if (e instanceof RestApiUnauthorizedException) {
                return "redirect:/sign-in";
            }
            redirectAttributes.addFlashAttribute("addMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("addAlertClass", "alert-danger");
            return "redirect:/profile";
        }

        return "redirect:/profile";
    }

    @PostMapping("/profile/removeSubscription")
    public String removeSubscription(
            @ModelAttribute("removeSubscription")   UserSubscriptionsDto removeSubscription,
                                                    HttpSession session,
                                                    RedirectAttributes redirectAttributes) {
        try {
            service.removeSubscriptions(removeSubscription.getQuery(), (String) session.getAttribute("accessToken"));
            redirectAttributes.addFlashAttribute("removeMessage", "Successfully removed");
            redirectAttributes.addFlashAttribute("removeAlertClass", "alert-success");

        } catch (RestApiException e) {
            if (e instanceof RestApiUnauthorizedException) {
                return "redirect:/sign-in";
            }
            redirectAttributes.addFlashAttribute("removeMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("removeAlertClass", "alert-danger");
            return "redirect:/profile";
        }

        return "redirect:/profile";
    }
}
