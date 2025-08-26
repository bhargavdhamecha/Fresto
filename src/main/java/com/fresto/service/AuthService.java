package com.fresto.service;

import com.fresto.dto.UserRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for handling authentication-related operations.
 */
@Slf4j
@Service
public class AuthService {

    private final AuthenticationManager authManager;

    public AuthService(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @Autowired
    SecurityContextRepository securityContextRepository;

    public void autoLogin(HttpServletRequest request, UserRequestDTO  requestDTO, HttpServletResponse response) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword());
            authToken.setDetails(new WebAuthenticationDetails(request));

            Authentication authentication = authManager.authenticate(authToken);

            SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
                    .getContextHolderStrategy();

            SecurityContext context = securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authentication);
            securityContextHolderStrategy.setContext(context);

            securityContextRepository.saveContext(context, request, response);
        } catch (Exception e) {
            log.error("Auto-login failed for user: {}", requestDTO.getEmail(), e);
            throw new RuntimeException(e);
        }
    }
}
