package com.sems.service;

import com.sems.dto.*;
import com.sems.entity.*;
import com.sems.repository.UserRepository;
import com.sems.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authManager;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public AuthResponse login(LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        var u = userRepo.findByUsername(req.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new AuthResponse(jwt.generate(u.getUsername(), u.getRole().name()),
            u.getUsername(), u.getRole().name());
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepo.existsByUsername(req.getUsername()))
            throw new IllegalArgumentException("Username already taken");
        if (userRepo.existsByEmail(req.getEmail()))
            throw new IllegalArgumentException("Email already registered");
        Role role = (req.getRole() != null && req.getRole().equalsIgnoreCase("ADMIN")) ? Role.ADMIN : Role.EMPLOYEE;
        var u = userRepo.save(User.builder()
            .username(req.getUsername()).email(req.getEmail())
            .password(encoder.encode(req.getPassword()))
            .role(role).enabled(true).build());
        return new AuthResponse(jwt.generate(u.getUsername(), u.getRole().name()),
            u.getUsername(), u.getRole().name());
    }
}
