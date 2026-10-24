package com.example.movieticketbooking;

import com.example.movieticketbooking.dto.auth.SigninRequest;
import com.example.movieticketbooking.dto.auth.SignupRequest;
import com.example.movieticketbooking.exception.BadRequestException;
import com.example.movieticketbooking.exception.ConflictException;
import com.example.movieticketbooking.repository.UserRepository;
import com.example.movieticketbooking.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void signup_shouldCreateCustomerAndReturnToken() {
        SignupRequest request = new SignupRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("Password123");

        var response = authService.signup(request);

        assertNotNull(response.getToken());
        assertTrue(userRepository.findByEmail("john@example.com").isPresent());
    }

    @Test
    void signup_shouldFailForDuplicateEmail() {
        SignupRequest request = new SignupRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("Password123");
        authService.signup(request);

        assertThrows(ConflictException.class, () -> authService.signup(request));
    }

    @Test
    void signin_shouldReturnTokenForValidCredentials() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setName("Jane Doe");
        signupRequest.setEmail("jane@example.com");
        signupRequest.setPassword("Password123");
        authService.signup(signupRequest);

        SigninRequest signinRequest = new SigninRequest();
        signinRequest.setEmail("jane@example.com");
        signinRequest.setPassword("Password123");

        var response = authService.signin(signinRequest);

        assertNotNull(response.getToken());
    }

    @Test
    void signin_shouldFailForInvalidPassword() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setName("Jane Doe");
        signupRequest.setEmail("jane@example.com");
        signupRequest.setPassword("Password123");
        authService.signup(signupRequest);

        SigninRequest signinRequest = new SigninRequest();
        signinRequest.setEmail("jane@example.com");
        signinRequest.setPassword("WrongPass");

        assertThrows(BadRequestException.class, () -> authService.signin(signinRequest));
    }
}
