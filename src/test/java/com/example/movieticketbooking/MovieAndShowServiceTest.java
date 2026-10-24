package com.example.movieticketbooking;

import com.example.movieticketbooking.dto.movie.MovieRequest;
import com.example.movieticketbooking.dto.show.ShowRequest;
import com.example.movieticketbooking.entity.Movie;
import com.example.movieticketbooking.entity.Theatre;
import com.example.movieticketbooking.enums.MovieStatus;
import com.example.movieticketbooking.enums.ShowStatus;
import com.example.movieticketbooking.enums.TheatreStatus;
import com.example.movieticketbooking.exception.ConflictException;
import com.example.movieticketbooking.repository.MovieRepository;
import com.example.movieticketbooking.repository.ShowRepository;
import com.example.movieticketbooking.repository.TheatreRepository;
import com.example.movieticketbooking.service.MovieService;
import com.example.movieticketbooking.service.ShowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MovieAndShowServiceTest {

    @Autowired
    private MovieService movieService;

    @Autowired
    private ShowService showService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private ShowRepository showRepository;

    @BeforeEach
    void setUp() {
        showRepository.deleteAll();
        movieRepository.deleteAll();
        theatreRepository.deleteAll();
    }

    @Test
    void adminCanCreateMovieAndPublicCanViewIt() {
        MovieRequest request = new MovieRequest();
        request.setTitle("Inception");
        request.setDescription("Sci-fi thriller");
        request.setDuration(148);
        request.setLanguage("English");
        request.setGenre("Sci-Fi");
        request.setReleaseDate(LocalDate.of(2024, 1, 10));
        request.setStatus(MovieStatus.NOW_SHOWING);

        var response = movieService.createMovie(request);

        assertNotNull(response.getId());
        assertEquals(1, movieService.getAllMovies().size());
    }

    @Test
    void endedMovieCannotReceiveNewShow() {
        Movie movie = movieRepository.save(new Movie("Old Movie", "Desc", 120, "English", "Drama", LocalDate.of(2023, 1, 1), MovieStatus.ENDED));
        Theatre theatre = theatreRepository.save(new Theatre("Main Hall", "Colombo", 40, TheatreStatus.ACTIVE));

        ShowRequest request = new ShowRequest();
        request.setMovieId(movie.getId());
        request.setTheatreId(theatre.getId());
        request.setShowDate(LocalDate.now().plusDays(2));
        request.setShowTime(LocalTime.of(18, 30));
        request.setTicketPrice(new BigDecimal("1500"));
        request.setStatus(ShowStatus.SCHEDULED);

        assertThrows(ConflictException.class, () -> showService.createShow(request));
    }
}
