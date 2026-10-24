package com.example.movieticketbooking.service;

import com.example.movieticketbooking.dto.user.UserResponse;
import com.example.movieticketbooking.dto.user.UserUpdateRequest;
import com.example.movieticketbooking.entity.User;
import com.example.movieticketbooking.exception.BadRequestException;
import com.example.movieticketbooking.exception.ConflictException;
import com.example.movieticketbooking.exception.ResourceNotFoundException;
import com.example.movieticketbooking.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return toResponse(user);
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (!existing.getEmail().equals(request.getEmail()) && userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("Email already exists");
        }

        existing.setName(request.getName());
        existing.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User saved = userRepository.save(existing);
        log.info("User updated: {}", saved.getEmail());
        return toResponse(saved);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
        log.info("User deleted: {}", user.getEmail());
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
