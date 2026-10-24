package com.example.movieticketbooking.service;

import com.example.movieticketbooking.dto.movie.MovieRequest;
import com.example.movieticketbooking.dto.movie.MovieResponse;
import com.example.movieticketbooking.entity.Movie;
import com.example.movieticketbooking.enums.MovieStatus;
import com.example.movieticketbooking.exception.BadRequestException;
import com.example.movieticketbooking.exception.ConflictException;
import com.example.movieticketbooking.exception.ResourceNotFoundException;
import com.example.movieticketbooking.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private static final Logger log = LoggerFactory.getLogger(MovieService.class);

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public MovieResponse createMovie(MovieRequest request) {
        Movie movie = new Movie(
                request.getTitle(),
                request.getDescription(),
                request.getDuration(),
                request.getLanguage(),
                request.getGenre(),
                request.getReleaseDate(),
                request.getStatus()
        );

        Movie saved = movieRepository.save(movie);
        log.info("Movie created: {}", saved.getTitle());
        return toResponse(saved);
    }

    public List<MovieResponse> getAllMovies() {
        return movieRepository.findAll().stream().map(this::toResponse).toList();
    }

    public MovieResponse getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        return toResponse(movie);
    }

    public MovieResponse updateMovie(Long id, MovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));

        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setDuration(request.getDuration());
        movie.setLanguage(request.getLanguage());
        movie.setGenre(request.getGenre());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setStatus(request.getStatus());

        Movie saved = movieRepository.save(movie);
        log.info("Movie updated: {}", saved.getTitle());
        return toResponse(saved);
    }

    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        movieRepository.delete(movie);
        log.info("Movie deleted: {}", movie.getTitle());
    }

    public void validateMovieCanAcceptNewShow(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieId));
        if (MovieStatus.ENDED.equals(movie.getStatus())) {
            throw new ConflictException("Ended movies cannot receive new shows");
        }
    }

    private MovieResponse toResponse(Movie movie) {
        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getDuration(),
                movie.getLanguage(),
                movie.getGenre(),
                movie.getReleaseDate(),
                movie.getStatus()
        );
    }
}
