package com.aura.controller;

import com.aura.model.User;
import com.aura.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // =========================
    // REGISTER
    // =========================
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody Map<String, String> req) {

        try {
            String email = req.get("email");
            String password = req.get("password");

            User user = authService.register(email, password);

            return ResponseEntity.ok(
                    Map.of(
                            "message", "Registration successful",
                            "email", user.getEmail(),
                            "role", user.getRole().name()
                    )
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message", e.getMessage()
                    ));
        }
    }

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> req) {

        try {
            String email = req.get("email");
            String password = req.get("password");

            String token = authService.login(email, password);

            User user = authService.findByEmail(email);

            return ResponseEntity.ok(
                    Map.of(
                            "token", token,
                            "email", user.getEmail(),
                            "role", user.getRole().name()
                    )
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message", e.getMessage()
                    ));
        }
    }

    // =========================
    // CURRENT USER
    // =========================
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                Map.of(
                        "email", user.getEmail(),
                        "role", user.getRole().name()
                )
        );
    }
}