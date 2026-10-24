package com.example.movieticketbooking.controller;

import com.example.movieticketbooking.dto.booking.BookingRequest;
import com.example.movieticketbooking.dto.booking.BookingResponse;
import com.example.movieticketbooking.enums.BookingStatus;
import com.example.movieticketbooking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/bookings")
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.createBooking(authentication.getName(), request));
    }

    @GetMapping("/bookings/{id}")
    public BookingResponse getBookingById(@PathVariable Long id, Authentication authentication) {
        return bookingService.getBookingById(id, authentication.getName());
    }

    @GetMapping("/bookings")
    public List<BookingResponse> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/bookings/my-bookings")
    public List<BookingResponse> getMyBookings(Authentication authentication) {
        return bookingService.getMyBookings(authentication.getName());
    }

    @PutMapping("/bookings/{id}/cancel")
    public BookingResponse cancelBooking(@PathVariable Long id, Authentication authentication) {
        return bookingService.cancelBooking(id, authentication.getName());
    }

    @PutMapping("/bookings/{id}/status")
    public BookingResponse updateBookingStatus(@PathVariable Long id, @RequestParam BookingStatus status) {
        return bookingService.updateBookingStatus(id, status);
    }
}
