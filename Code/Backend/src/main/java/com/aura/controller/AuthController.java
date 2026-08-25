package com.aura.controller;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.aura.service.AuthService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
     @PostMapping("/login")
     public Map<String,String> login(@RequestBody Map<String,String> req){
       return Map.of(
               "token","demo-token",
               "name",req.getOrDefault("email","User"),"role","PATIENT");
     }
     @PostMapping("/register")
     public Map<String,String> register(@RequestBody Map<String,String> req){
         String email = req.get("email");
         String password = req.get("password");
         authService.register(email, password);
       return Map.of(
               "message","Registration successful",
               "email",req.getOrDefault("email",""));
     }
     private final AuthService authService;
     public AuthController(AuthService authService) {
         this.authService = authService;
     }
}