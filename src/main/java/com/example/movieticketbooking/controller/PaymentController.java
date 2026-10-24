package com.example.movieticketbooking.controller;

import com.example.movieticketbooking.dto.payment.PaymentRequest;
import com.example.movieticketbooking.dto.payment.PaymentResponse;
import com.example.movieticketbooking.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payments")
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.processPayment(request, authentication.getName()));
    }

    @GetMapping("/payments")
    public List<PaymentResponse> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/payments/{id}")
    public PaymentResponse getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @GetMapping("/payments/{id}/status")
    public PaymentResponse getPaymentStatus(@PathVariable Long id) {
        return paymentService.getPaymentStatus(id);
    }
}
