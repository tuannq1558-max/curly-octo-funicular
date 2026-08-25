package com.aura.service;

import com.aura.model.User;
import com.aura.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository; // final: 1 khi constructor da gan thi se khong gan vao repo nao nua
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;}

    public User register(String email, String password) {
        if(userRepository.findByEmail(email).isPresent()){
            throw new IllegalArgumentException("Email already exists");
        }
        String passwordHash = passwordEncoder.encode(password);
        User user = new User();
        user.setEmail(email);

    }
}