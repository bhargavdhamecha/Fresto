package com.fresto.entity;

import com.fresto.constant.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * User entity represents a user in the e-commerce application.
 * It contains details such as user ID, name, email, password, type, and associated orders.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_master", uniqueConstraints = {@UniqueConstraint(name = "unique_usermail", columnNames = {"userEmail"})})
public class User {

    @Id
    @Column(unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @NotNull
    private String userEmail;

    @NotNull
    private String userName;

    @NotNull
    private String userPassword;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Orders> orders;

    @Override
    public String toString() {
        return "User [userId=" + userId + ", userName=" + userName + ", userEmail=" + userEmail +
                ", userType=" + userType + "]";
    }

    public User(String userEmail, String password, String userName, UserType userType) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPassword = password;
        this.userType = userType;
    }
}


