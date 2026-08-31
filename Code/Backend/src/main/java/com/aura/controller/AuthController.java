package com.aura.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

import com.aura.service.AuthService;
import com.aura.model.User;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Map<String, String> register(
            @RequestBody Map<String, String> req) {

        String email = req.get("email");
        String password = req.get("password");

        authService.register(email, password);

        return Map.of(
                "message", "Registration successful",
                "email", email
        );
    }

    @PostMapping("/login")
    public Map<String, String> login(
            @RequestBody Map<String, String> req) {

        String email = req.get("email");
        String password = req.get("password");

        try {
            User user = authService.login(email, password);

            return Map.of(
                    "message", "Login successful",
                    "email", user.getEmail(),
                    "role", "PATIENT"
            );

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    e.getMessage()
            );
        }
    }
}