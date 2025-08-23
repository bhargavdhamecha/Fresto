package com.fresto.service;

import com.fresto.entity.User;
import com.fresto.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository repository) {
        this.userRepository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmail(userEmail);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + userEmail);
        }

        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUsername(user.getUserName());
        userDetails.setId(user.getUserId());
        userDetails.setPassword(user.getUserPassword());

        String role = "ROLE_" + user.getUserType().name();  // Spring Security expects "ROLE_" prefix
        userDetails.setAuthorities(List.of(new SimpleGrantedAuthority(role)));
        userDetails.setRoles(List.of(user.getUserType().name()));

        return userDetails;
    }
}
