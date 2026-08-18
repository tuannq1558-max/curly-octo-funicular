package com.aura.controller;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
 @PostMapping("/login") public Map login(@RequestBody Map<String,String> req){
   return Map.of("token","demo-token","name",req.getOrDefault("email","User"),"role","PATIENT");
 }
 @PostMapping("/register") public Map register(@RequestBody Map<String,String> req){
   return Map.of("message","Registration successful","email",req.getOrDefault("email",""));
 }
}