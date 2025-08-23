package com.fresto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for user login requests.
 * Contains fields for user name, email, and password.
 */
@Getter
@Setter
public class LoginRequestDTO {
    @NotNull
    @Email(message = "Email not valid")
    private String email;

    @NotBlank
    @Size(min = 4, message = "password length should be in range of 4 to 8")
    private String password;
}
