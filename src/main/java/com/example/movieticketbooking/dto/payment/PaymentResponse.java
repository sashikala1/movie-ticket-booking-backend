package com.example.movieticketbooking.dto.payment;

import com.example.movieticketbooking.enums.PaymentMethod;
import com.example.movieticketbooking.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {
    private Long id;
    private Long bookingId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;

    public PaymentResponse() {
    }

    public PaymentResponse(Long id, Long bookingId, BigDecimal amount, LocalDateTime paymentDate, PaymentMethod paymentMethod, PaymentStatus status) {
        this.id = id;
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
