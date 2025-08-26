package com.fresto.controller;

import com.fresto.dto.UserRequestDTO;
import com.fresto.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.fresto.constant.ApiConstant.*;
import static com.fresto.constant.AppConstant.*;

/**
 * AuthController is responsible for handling authentication-related requests.
 */
@Slf4j
@RequestMapping(AUTH_CONTROLLER)
@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService authService) {
        this.userService = authService;
    }

    @PostMapping(REGISTER_USER)
    public String register(@Valid @ModelAttribute UserRequestDTO registrationRequest, HttpServletRequest request, HttpServletResponse response) {
        // Logic for registering a new user
        try {
            log.debug("enter --> Registering user with email: {}", registrationRequest.getEmail());
            userService.registerUser(registrationRequest, request, response);
            return REDIRECT_TO + HOME_PAGE;
        } catch (Exception e) {
            log.error("Error during registration: {}", e.getMessage());
            return REDIRECT_TO + EXCEPTION_PAGE;
        }
    }

    @PostMapping(LOGOUT)
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate(); // Invalidate session
        log.debug("user logged out successfully");
        redirectAttributes.addFlashAttribute(" ", "You have been logged out successfully.");
        return REDIRECT_TO + LOGIN_PAGE;
    }
}
