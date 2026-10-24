package com.example.movieticketbooking;

import com.example.movieticketbooking.dto.auth.SignupRequest;
import com.example.movieticketbooking.dto.booking.BookingRequest;
import com.example.movieticketbooking.dto.payment.PaymentRequest;
import com.example.movieticketbooking.entity.Movie;
import com.example.movieticketbooking.entity.Show;
import com.example.movieticketbooking.entity.Theatre;
import com.example.movieticketbooking.entity.User;
import com.example.movieticketbooking.enums.*;
import com.example.movieticketbooking.repository.*;
import com.example.movieticketbooking.service.AuthService;
import com.example.movieticketbooking.service.BookingService;
import com.example.movieticketbooking.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookingAndPaymentServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
        bookingRepository.deleteAll();
        showRepository.deleteAll();
        movieRepository.deleteAll();
        theatreRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void customerCanCreateBookingAndPaymentConfirmsIt() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setName("Alice");
        signupRequest.setEmail("alice@example.com");
        signupRequest.setPassword("Password123");
        authService.signup(signupRequest);

        User user = userRepository.findByEmail("alice@example.com").orElseThrow();
        Movie movie = movieRepository.save(new Movie("Test Movie", "Desc", 120, "English", "Drama", LocalDate.now(), MovieStatus.NOW_SHOWING));
        Theatre theatre = theatreRepository.save(new Theatre("VIP Hall", "Kandy", 30, TheatreStatus.ACTIVE));
        Show show = showRepository.save(new Show(movie, theatre, LocalDate.now().plusDays(1), LocalTime.of(19, 0), new BigDecimal("1000"), ShowStatus.SCHEDULED));

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setShowId(show.getId());
        bookingRequest.setSeatNumbers(java.util.List.of("A1", "A2"));
        bookingRequest.setNumberOfTickets(2);

        var booking = bookingService.createBooking(user.getEmail(), bookingRequest);
        assertEquals(2, booking.getSeatNumbers().size());

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setBookingId(booking.getId());
        paymentRequest.setAmount(new BigDecimal("2000"));
        paymentRequest.setPaymentMethod(PaymentMethod.CARD);

        var payment = paymentService.processPayment(paymentRequest, user.getEmail());
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
        assertEquals(BookingStatus.CONFIRMED, bookingService.findBookingEntityById(booking.getId()).getStatus());
    }

    @Test
    void duplicateSeatBookingIsRejected() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setName("Bob");
        signupRequest.setEmail("bob@example.com");
        signupRequest.setPassword("Password123");
        authService.signup(signupRequest);

        User user = userRepository.findByEmail("bob@example.com").orElseThrow();
        Movie movie = movieRepository.save(new Movie("Movie Two", "Desc", 130, "English", "Action", LocalDate.now(), MovieStatus.NOW_SHOWING));
        Theatre theatre = theatreRepository.save(new Theatre("Cinema 2", "Colombo", 20, TheatreStatus.ACTIVE));
        Show show = showRepository.save(new Show(movie, theatre, LocalDate.now().plusDays(2), LocalTime.of(20, 0), new BigDecimal("1200"), ShowStatus.SCHEDULED));

        BookingRequest firstRequest = new BookingRequest();
        firstRequest.setShowId(show.getId());
        firstRequest.setSeatNumbers(java.util.List.of("B1", "B2"));
        firstRequest.setNumberOfTickets(2);
        bookingService.createBooking(user.getEmail(), firstRequest);

        BookingRequest secondRequest = new BookingRequest();
        secondRequest.setShowId(show.getId());
        secondRequest.setSeatNumbers(java.util.List.of("B2", "B3"));
        secondRequest.setNumberOfTickets(2);

        assertThrows(RuntimeException.class, () -> bookingService.createBooking(user.getEmail(), secondRequest));
    }
}
