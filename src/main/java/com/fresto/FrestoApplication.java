package com.fresto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * FrestoApplication is the main entry point for the Fresto application.
 * It starts the Spring Boot application and excludes the default security configuration.
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class FrestoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrestoApplication.class, args);
    }

}
