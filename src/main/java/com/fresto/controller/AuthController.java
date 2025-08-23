package com.fresto.controller;

import com.fresto.constant.ApiConstant;
import com.fresto.dto.UserRequestDTO;
import com.fresto.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.fresto.constant.AppConstant.EXCEPTION_PAGE;

/**
 * AuthController is responsible for handling authentication-related requests.
 */
@Slf4j
@RequestMapping("/auth")
@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService authService) {
        this.userService = authService;
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute UserRequestDTO registrationRequest) {
        // Logic for registering a new user
        try {
            log.debug("enter --> Registering user with email: {}", registrationRequest.getEmail());
            userService.registerUser(registrationRequest);
            return "redirect:" + ApiConstant.HOME_PATH;
        } catch (Exception e) {
            log.error("Error during registration: {}", e.getMessage());
            return "redirect:/" + EXCEPTION_PAGE;
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate session
        log.debug("user logged out successfully");
        return "redirect:/login?logout";
    }
}
