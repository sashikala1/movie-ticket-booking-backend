package com.example.movieticketbooking.controller;

import com.example.movieticketbooking.dto.show.SeatAvailabilityResponse;
import com.example.movieticketbooking.dto.show.ShowRequest;
import com.example.movieticketbooking.dto.show.ShowResponse;
import com.example.movieticketbooking.service.ShowService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ShowController {

    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @PostMapping("/shows")
    public ResponseEntity<ShowResponse> createShow(@Valid @RequestBody ShowRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(showService.createShow(request));
    }

    @GetMapping("/shows")
    public List<ShowResponse> getAllShows() {
        return showService.getAllShows();
    }

    @GetMapping("/shows/{id}")
    public ShowResponse getShowById(@PathVariable Long id) {
        return showService.getShowById(id);
    }

    @PutMapping("/shows/{id}")
    public ShowResponse updateShow(@PathVariable Long id, @Valid @RequestBody ShowRequest request) {
        return showService.updateShow(id, request);
    }

    @DeleteMapping("/shows/{id}")
    public ResponseEntity<Void> deleteShow(@PathVariable Long id) {
        showService.deleteShow(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/shows/movie/{movieId}")
    public List<ShowResponse> getShowsByMovieId(@PathVariable Long movieId) {
        return showService.getShowsByMovieId(movieId);
    }

    @GetMapping("/shows/{showId}/seats")
    public SeatAvailabilityResponse getSeatAvailability(@PathVariable Long showId) {
        return showService.getSeatAvailability(showId);
    }
}
