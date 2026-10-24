package com.example.movieticketbooking.controller;

import com.example.movieticketbooking.dto.theatre.TheatreRequest;
import com.example.movieticketbooking.dto.theatre.TheatreResponse;
import com.example.movieticketbooking.service.TheatreService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TheatreController {

    private final TheatreService theatreService;

    public TheatreController(TheatreService theatreService) {
        this.theatreService = theatreService;
    }

    @PostMapping("/theatres")
    public ResponseEntity<TheatreResponse> createTheatre(@Valid @RequestBody TheatreRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(theatreService.createTheatre(request));
    }

    @GetMapping("/theatres")
    public List<TheatreResponse> getAllTheatres() {
        return theatreService.getAllTheatres();
    }

    @GetMapping("/theatres/{id}")
    public TheatreResponse getTheatreById(@PathVariable Long id) {
        return theatreService.getTheatreById(id);
    }

    @PutMapping("/theatres/{id}")
    public TheatreResponse updateTheatre(@PathVariable Long id, @Valid @RequestBody TheatreRequest request) {
        return theatreService.updateTheatre(id, request);
    }

    @DeleteMapping("/theatres/{id}")
    public ResponseEntity<Void> deleteTheatre(@PathVariable Long id) {
        theatreService.deleteTheatre(id);
        return ResponseEntity.noContent().build();
    }
}
