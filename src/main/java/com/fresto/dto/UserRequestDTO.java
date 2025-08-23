package com.fresto.dto;

import jakarta.persistence.Column;
import jakarta.validation.Constraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * DTO for user registration requests.
 * Contains fields for user name, email, and password.
 */

@ToString
public class UserRequestDTO {
    @Getter
    @Setter
    @NotNull(message = "username is null")
    @NotBlank(message = "username is blank")
    private String name;

    @Getter
    @Setter
    @Email(message = "Email not valid")
    private String email;

    @Getter
    @Setter
    @NotNull
    private String password;


    private boolean adminRights;

    public boolean getAdminRights() {
        return adminRights;
    }

    public void setAdminRights(boolean adminRights) {
        this.adminRights = adminRights;
    }
}
