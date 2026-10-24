package com.example.movieticketbooking.service;

import com.example.movieticketbooking.dto.booking.BookingRequest;
import com.example.movieticketbooking.dto.booking.BookingResponse;
import com.example.movieticketbooking.entity.Booking;
import com.example.movieticketbooking.entity.Show;
import com.example.movieticketbooking.entity.User;
import com.example.movieticketbooking.enums.BookingStatus;
import com.example.movieticketbooking.enums.ShowStatus;
import com.example.movieticketbooking.exception.BadRequestException;
import com.example.movieticketbooking.exception.ConflictException;
import com.example.movieticketbooking.exception.ResourceNotFoundException;
import com.example.movieticketbooking.repository.BookingRepository;
import com.example.movieticketbooking.repository.ShowRepository;
import com.example.movieticketbooking.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final ShowRepository showRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, ShowRepository showRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.showRepository = showRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BookingResponse createBooking(String customerEmail, BookingRequest request) {
        if (request.getSeatNumbers() == null || request.getSeatNumbers().isEmpty()) {
            throw new BadRequestException("At least one seat is required");
        }

        if (request.getNumberOfTickets() == null || request.getNumberOfTickets() < 1) {
            throw new BadRequestException("Number of tickets must be at least 1");
        }

        if (!request.getNumberOfTickets().equals(request.getSeatNumbers().size())) {
            throw new BadRequestException("Number of tickets must match the number of selected seats");
        }

        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + request.getShowId()));

        if (show.getStatus() == ShowStatus.CANCELLED) {
            throw new BadRequestException("Cancelled shows cannot accept bookings");
        }

        if (show.getStatus() == ShowStatus.COMPLETED) {
            throw new BadRequestException("Completed shows cannot accept new bookings");
        }

        if (show.getMovie() == null || show.getTheatre() == null) {
            throw new BadRequestException("Show is invalid");
        }

        if (show.getTheatre().getStatus() != null && show.getTheatre().getStatus().name().equals("INACTIVE")) {
            throw new BadRequestException("Theatre is inactive");
        }

        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + customerEmail));

        List<String> requestedSeats = parseSeatListFromRequest(request.getSeatNumbers());
        Set<String> requestedSeatSet = new HashSet<>(requestedSeats);
        if (requestedSeatSet.size() != requestedSeats.size()) {
            throw new BadRequestException("Duplicate seat selections are not allowed");
        }

        List<Booking> activeBookings = bookingRepository.findActiveByShowId(show.getId());
        Set<String> bookedSeats = new HashSet<>();
        for (Booking booking : activeBookings) {
            bookedSeats.addAll(parseSeatListFromString(booking.getSeatNumbers()));
        }

        if (!bookedSeats.isEmpty()) {
            List<String> duplicates = requestedSeats.stream()
                    .filter(bookedSeats::contains)
                    .distinct()
                    .toList();
            if (!duplicates.isEmpty()) {
                throw new ConflictException("Requested seats are already booked: " + duplicates);
            }
        }

        if (requestedSeats.size() > show.getTheatre().getCapacity()) {
            throw new BadRequestException("Requested seat count exceeds theatre capacity");
        }

        String seatString = String.join(",", requestedSeats);
        BigDecimal totalAmount = show.getTicketPrice().multiply(BigDecimal.valueOf(request.getNumberOfTickets()));

        Booking booking = new Booking(
                customer,
                show,
                seatString,
                request.getNumberOfTickets(),
                totalAmount,
                LocalDateTime.now(),
                BookingStatus.PENDING
        );

        Booking saved = bookingRepository.save(booking);
        log.info("Booking created for customer {} for show {}", customer.getEmail(), show.getId());
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long id, String currentEmail) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        if (!booking.getUser().getEmail().equals(currentEmail)) {
            throw new BadRequestException("You can only access your own bookings");
        }

        return toResponse(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return bookingRepository.findByUserId(user.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public BookingResponse cancelBooking(Long id, String currentEmail) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        if (!booking.getUser().getEmail().equals(currentEmail)) {
            throw new BadRequestException("You can only cancel your own bookings");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking is already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Booking saved = bookingRepository.save(booking);
        log.info("Booking cancelled: {}", saved.getId());
        return toResponse(saved);
    }

    @Transactional
    public BookingResponse updateBookingStatus(Long id, BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        booking.setStatus(status);
        Booking saved = bookingRepository.save(booking);
        log.info("Booking status updated to {} for booking {}", status, saved.getId());
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Booking findBookingEntityById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
    }

    @Transactional(readOnly = true)
    public List<String> getBookedSeatsForShow(Long showId) {
        return bookingRepository.findActiveByShowId(showId).stream()
                .flatMap(booking -> parseSeatListFromString(booking.getSeatNumbers()).stream())
                .distinct()
                .sorted()
                .toList();
    }

    private List<String> parseSeatListFromRequest(List<String> seatNumbers) {
        if (seatNumbers == null || seatNumbers.isEmpty()) {
            return new ArrayList<>();
        }
        return seatNumbers.stream()
                .filter(java.util.Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }

    private List<String> parseSeatListFromString(String seatNumbers) {
        if (seatNumbers == null || seatNumbers.isBlank()) {
            return new ArrayList<>();
        }
        return java.util.Arrays.stream(seatNumbers.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }

    private BookingResponse toResponse(Booking booking) {
        List<String> seatNumbers = parseSeatListFromString(booking.getSeatNumbers());
        return new BookingResponse(
                booking.getId(),
                booking.getUser().getId(),
                booking.getShow().getId(),
                booking.getShow().getMovie().getTitle(),
                booking.getShow().getTheatre().getName(),
                seatNumbers,
                booking.getNumberOfTickets(),
                booking.getTotalAmount(),
                booking.getBookingDate(),
                booking.getStatus()
        );
    }
}
