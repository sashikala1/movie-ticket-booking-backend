package com.example.movieticketbooking.service;

import com.example.movieticketbooking.dto.payment.PaymentRequest;
import com.example.movieticketbooking.dto.payment.PaymentResponse;
import com.example.movieticketbooking.entity.Booking;
import com.example.movieticketbooking.entity.Payment;
import com.example.movieticketbooking.enums.BookingStatus;
import com.example.movieticketbooking.enums.PaymentMethod;
import com.example.movieticketbooking.enums.PaymentStatus;
import com.example.movieticketbooking.exception.BadRequestException;
import com.example.movieticketbooking.exception.ConflictException;
import com.example.movieticketbooking.exception.ResourceNotFoundException;
import com.example.movieticketbooking.repository.BookingRepository;
import com.example.movieticketbooking.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public PaymentService(PaymentRepository paymentRepository, BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public PaymentResponse processPayment(PaymentRequest request, String currentEmail) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + request.getBookingId()));

        if (!booking.getUser().getEmail().equals(currentEmail)) {
            throw new BadRequestException("You can only pay for your own bookings");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Cancelled bookings cannot be paid");
        }

        Optional<Payment> existingPayment = paymentRepository.findByBookingId(booking.getId());
        if (existingPayment.isPresent() && existingPayment.get().getStatus() == PaymentStatus.COMPLETED) {
            throw new BadRequestException("Completed payment cannot be processed again");
        }

        if (request.getAmount() == null || request.getAmount().compareTo(booking.getTotalAmount()) != 0) {
            throw new BadRequestException("Payment amount must match booking total amount");
        }

        Payment payment = new Payment(
                booking,
                request.getAmount(),
                request.getPaymentMethod(),
                PaymentStatus.PENDING,
                LocalDateTime.now()
        );

        paymentRepository.save(payment);

        payment.setStatus(PaymentStatus.COMPLETED);
        booking.setStatus(BookingStatus.CONFIRMED);
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepository.save(payment);
        bookingRepository.save(booking);

        log.info("Payment processed successfully for booking {}", booking.getId());
        return toResponse(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));
        return toResponse(payment);
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentStatus(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));
        return toResponse(payment);
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByBookingId(Long bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking id: " + bookingId));
        return toResponse(payment);
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getBooking().getId(),
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getPaymentMethod(),
                payment.getStatus()
        );
    }
}
