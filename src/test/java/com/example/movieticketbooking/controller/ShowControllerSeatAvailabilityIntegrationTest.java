package com.example.movieticketbooking.controller;

import com.example.movieticketbooking.entity.Movie;
import com.example.movieticketbooking.entity.Show;
import com.example.movieticketbooking.entity.Theatre;
import com.example.movieticketbooking.enums.MovieStatus;
import com.example.movieticketbooking.enums.ShowStatus;
import com.example.movieticketbooking.enums.TheatreStatus;
import com.example.movieticketbooking.repository.MovieRepository;
import com.example.movieticketbooking.repository.ShowRepository;
import com.example.movieticketbooking.repository.TheatreRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ShowControllerSeatAvailabilityIntegrationTest {

    @Autowired
    private ShowController showController;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void getSeatAvailability_shouldLoadTheatreWithoutLazyInitializationException() {
        Movie movie = movieRepository.save(new Movie(
                "Test Movie",
                "Description",
                120,
                "English",
                "Action",
                LocalDate.now().plusDays(1),
                MovieStatus.UPCOMING
        ));

        Theatre theatre = theatreRepository.save(new Theatre(
                "Main Hall",
                "Downtown",
                10,
                TheatreStatus.ACTIVE
        ));

        Show show = showRepository.save(new Show(
                movie,
                theatre,
                LocalDate.now().plusDays(1),
                LocalTime.of(18, 30),
                new BigDecimal("200.00"),
                ShowStatus.SCHEDULED
        ));

        entityManager.clear();

        var response = showController.getSeatAvailability(show.getId());

        assertNotNull(response);
        assertNotNull(response.getAvailableSeats());
        assertFalse(response.getAvailableSeats().isEmpty());
    }
}
