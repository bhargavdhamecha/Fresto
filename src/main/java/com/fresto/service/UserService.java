package com.fresto.service;

import com.fresto.constant.UserType;
import com.fresto.dto.UserRequestDTO;
import com.fresto.entity.User;
import com.fresto.exception.UserNotFoundException;
import com.fresto.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.StringUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * AuthService is responsible for handling authentication-related operations.
 */
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(rollbackOn = Exception.class)
    public boolean registerUser(UserRequestDTO registrationRequest) {
        log.debug("Enter --> registerUser with email: {}", registrationRequest.getEmail());
        User user = new User(registrationRequest.getEmail(), encodePassword(registrationRequest.getPassword()), registrationRequest.getName(), registrationRequest.getAdminRights() ? UserType.ADMIN : UserType.USER);
        userRepository.updateOrInsert(user);
        log.debug("User registered successfully with email: {}", registrationRequest.getEmail());
        return true;
    }

    private String encodePassword(String password) {
        // Use BCryptPasswordEncoder to hash the password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    public User findByEmail(String email) {
        return userRepository.findByUserEmail(email);
    }

    public List<User> getAllByUserType(UserType userType) {
        return userRepository.findAllByUserType(userType);
    }

    public boolean updateUser(UserRequestDTO requestDTO, int userId) throws UserNotFoundException {
        log.debug("Enter --> updateUser with userId: {}", userId);
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        log.debug("user found, requested change: {}", requestDTO);
        if (existingUser != null) {
            existingUser.setUserEmail(requestDTO.getEmail());
            existingUser.setUserName(requestDTO.getName());
            existingUser.setUserType(requestDTO.getAdminRights() ? UserType.ADMIN : UserType.USER);
            if (requestDTO.getPassword() != null && StringUtils.hasLength(requestDTO.getPassword())) {
                existingUser.setUserPassword(encodePassword(requestDTO.getPassword()));
            }
            User user = userRepository.updateOrInsert(existingUser);
            log.debug("User updated successfully with id: {}", user.getUserId());
            return true;
        }
        return false;
    }

    public boolean deleteUser(int userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        } else {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
    }

    public User getUserById(int userId) throws UserNotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

}
