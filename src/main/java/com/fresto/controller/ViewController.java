package com.fresto.controller;

import com.fresto.dto.LoginRequestDTO;
import com.fresto.dto.UserRequestDTO;
import com.fresto.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.fresto.constant.ApiConstant.*;
import static com.fresto.constant.AppConstant.*;

/**
 * HomeController is responsible for handling requests to the home page and other static pages.
 * It routes requests based on the servlet path and sets the appropriate content for the model.
 */
@Slf4j
@Controller
public class ViewController {


    @GetMapping(value = {HOME_PATH, ABOUT_PATH, PRODUCTS_PATH, LOGIN_PATH, BLOG_PATH, CONTACT_PATH, SIGN_UP_PATH})
    public String router(Model model, HttpServletRequest request) {
        String servletPath = request.getServletPath();
        String content = resolveContent(servletPath);
        model.addAttribute(PAGE_CONTENT, content);
        addFormAttributesIfNeeded(servletPath, model);
        log.debug("routing to page: {}", content);
        return content;
    }

    /**
     * Resolves the content view name based on the servlet path.
     */
    private String resolveContent(String servletPath) {
        return switch (servletPath) {
            case ABOUT_PATH -> ABOUT_PAGE;
            case LOGIN_PATH -> LOGIN_PAGE;
            case BLOG_PATH -> BLOG_PAGE;
            case CONTACT_PATH -> CONTACT_PAGE;
            case SIGN_UP_PATH -> SIGN_UP_PAGE;
            case HOME_PATH -> HOME_PAGE;
            default -> EXCEPTION_PAGE;
        };
    }

    /**
     * Adds form DTOs to the model if the path matches login or sign up.
     */
    private void addFormAttributesIfNeeded(String servletPath, Model model) {
        if (LOGIN_PATH.equals(servletPath)) {
            model.addAttribute("loginRequest", new LoginRequestDTO());
        }
        if (SIGN_UP_PATH.equals(servletPath)) {
            model.addAttribute("registrationRequest", new UserRequestDTO());
        }
    }


}
