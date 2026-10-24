package com.example.movieticketbooking.controller;

import com.example.movieticketbooking.entity.Booking;
import com.example.movieticketbooking.entity.Movie;
import com.example.movieticketbooking.entity.Show;
import com.example.movieticketbooking.entity.Theatre;
import com.example.movieticketbooking.entity.User;
import com.example.movieticketbooking.enums.BookingStatus;
import com.example.movieticketbooking.enums.MovieStatus;
import com.example.movieticketbooking.enums.Role;
import com.example.movieticketbooking.enums.ShowStatus;
import com.example.movieticketbooking.enums.TheatreStatus;
import com.example.movieticketbooking.repository.BookingRepository;
import com.example.movieticketbooking.repository.MovieRepository;
import com.example.movieticketbooking.repository.ShowRepository;
import com.example.movieticketbooking.repository.TheatreRepository;
import com.example.movieticketbooking.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BookingControllerBookingDetailsIntegrationTest {

    @Autowired
    private BookingController bookingController;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void getBookingById_shouldLoadBookingDetailsWithoutLazyInitializationException() {
        User customer = userRepository.save(new User("Alice", "alice@example.com", "secret", Role.CUSTOMER));

        Movie movie = movieRepository.save(new Movie(
                "Booking Test Movie",
                "Description",
                120,
                "English",
                "Action",
                LocalDate.now().plusDays(2),
                MovieStatus.UPCOMING
        ));

        Theatre theatre = theatreRepository.save(new Theatre(
                "VIP Hall",
                "Central",
                12,
                TheatreStatus.ACTIVE
        ));

        Show show = showRepository.save(new Show(
                movie,
                theatre,
                LocalDate.now().plusDays(2),
                LocalTime.of(19, 0),
                new BigDecimal("150.00"),
                ShowStatus.SCHEDULED
        ));

        Booking booking = bookingRepository.save(new Booking(
                customer,
                show,
                "A1,A2",
                2,
                new BigDecimal("300.00"),
                java.time.LocalDateTime.now(),
                BookingStatus.PENDING
        ));

        entityManager.clear();

        Authentication auth = new UsernamePasswordAuthenticationToken(customer.getEmail(), null);
        var response = bookingController.getBookingById(booking.getId(), auth);

        assertNotNull(response);
        assertEquals(customer.getEmail(), auth.getName());
        assertEquals(booking.getId(), response.getId());
    }
}
