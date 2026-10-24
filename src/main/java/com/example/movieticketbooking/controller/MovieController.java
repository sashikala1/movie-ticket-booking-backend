package com.example.movieticketbooking.controller;

import com.example.movieticketbooking.dto.movie.MovieRequest;
import com.example.movieticketbooking.dto.movie.MovieResponse;
import com.example.movieticketbooking.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/movies")
    public ResponseEntity<MovieResponse> createMovie(@Valid @RequestBody MovieRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.createMovie(request));
    }

    @GetMapping("/movies")
    public List<MovieResponse> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/movies/{id}")
    public MovieResponse getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @PutMapping("/movies/{id}")
    public MovieResponse updateMovie(@PathVariable Long id, @Valid @RequestBody MovieRequest request) {
        return movieService.updateMovie(id, request);
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
