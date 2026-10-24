package com.example.movieticketbooking.service;

import com.example.movieticketbooking.dto.auth.AuthResponse;
import com.example.movieticketbooking.dto.auth.SigninRequest;
import com.example.movieticketbooking.dto.auth.SignupRequest;
import com.example.movieticketbooking.entity.User;
import com.example.movieticketbooking.enums.Role;
import com.example.movieticketbooking.exception.BadRequestException;
import com.example.movieticketbooking.exception.ConflictException;
import com.example.movieticketbooking.repository.UserRepository;
import com.example.movieticketbooking.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse signup(SignupRequest request) {
        log.info("User registration requested for email: {}", request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail(), "ROLE_CUSTOMER");
        log.info("User registered successfully: {}", user.getEmail());
        return new AuthResponse(token);
    }

    public AuthResponse signin(SigninRequest request) {
        log.info("User login requested for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail(), "ROLE_" + user.getRole().name());
        log.info("User logged in successfully: {}", user.getEmail());
        return new AuthResponse(token);
    }
}
