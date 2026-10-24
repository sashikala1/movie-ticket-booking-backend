package com.example.movieticketbooking.service;

import com.example.movieticketbooking.dto.show.SeatAvailabilityResponse;
import com.example.movieticketbooking.dto.show.ShowRequest;
import com.example.movieticketbooking.dto.show.ShowResponse;
import com.example.movieticketbooking.entity.Movie;
import com.example.movieticketbooking.entity.Show;
import com.example.movieticketbooking.entity.Theatre;
import com.example.movieticketbooking.enums.ShowStatus;
import com.example.movieticketbooking.exception.BadRequestException;
import com.example.movieticketbooking.exception.ConflictException;
import com.example.movieticketbooking.exception.ResourceNotFoundException;
import com.example.movieticketbooking.repository.MovieRepository;
import com.example.movieticketbooking.repository.ShowRepository;
import com.example.movieticketbooking.repository.TheatreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShowService {

    private static final Logger log = LoggerFactory.getLogger(ShowService.class);

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;
    private final BookingService bookingService;

    public ShowService(ShowRepository showRepository, MovieRepository movieRepository, TheatreRepository theatreRepository, BookingService bookingService) {
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
        this.theatreRepository = theatreRepository;
        this.bookingService = bookingService;
    }

    @Transactional
    public ShowResponse createShow(ShowRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + request.getMovieId()));

        Theatre theatre = theatreRepository.findById(request.getTheatreId())
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + request.getTheatreId()));

        if (movie.getStatus() != null && movie.getStatus().name().equals("ENDED")) {
            throw new ConflictException("Movie has ended and cannot receive new shows");
        }

        if (theatre.getStatus() == null || !theatre.getStatus().name().equals("ACTIVE")) {
            throw new BadRequestException("Theatre must be active to schedule a show");
        }

        List<Show> conflictingShows = showRepository.findConflictingShows(
                theatre.getId(),
                request.getShowDate(),
                request.getShowTime()
        );

        if (!conflictingShows.isEmpty()) {
            throw new ConflictException("Theatre already has a conflicting show at the same date and time");
        }

        Show show = new Show(movie, theatre, request.getShowDate(), request.getShowTime(), request.getTicketPrice(), request.getStatus());
        Show saved = showRepository.save(show);
        log.info("Show created for movie {} at theatre {}", movie.getTitle(), theatre.getName());
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ShowResponse> getAllShows() {
        return showRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ShowResponse getShowById(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + id));
        return toResponse(show);
    }

    @Transactional(readOnly = true)
    public List<ShowResponse> getShowsByMovieId(Long movieId) {
        return showRepository.findByMovieId(movieId).stream().map(this::toResponse).toList();
    }

    public ShowResponse updateShow(Long id, ShowRequest request) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + id));

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + request.getMovieId()));

        Theatre theatre = theatreRepository.findById(request.getTheatreId())
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + request.getTheatreId()));

        show.setMovie(movie);
        show.setTheatre(theatre);
        show.setShowDate(request.getShowDate());
        show.setShowTime(request.getShowTime());
        show.setTicketPrice(request.getTicketPrice());
        show.setStatus(request.getStatus());

        Show saved = showRepository.save(show);
        log.info("Show updated: {}", saved.getId());
        return toResponse(saved);
    }

    public void deleteShow(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + id));
        showRepository.delete(show);
        log.info("Show deleted: {}", show.getId());
    }

    @Transactional(readOnly = true)
    public Show getShowEntityById(Long showId) {
        return showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + showId));
    }

    @Transactional(readOnly = true)
    public SeatAvailabilityResponse getSeatAvailability(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new BadRequestException("Show not found with id: " + showId));

        Theatre theatre = show.getTheatre();
        List<String> allSeats = generateSeats(theatre.getCapacity());
        List<String> bookedSeats = bookingService.getBookedSeatsForShow(showId);
        List<String> availableSeats = new ArrayList<>(allSeats);
        availableSeats.removeAll(bookedSeats);

        return new SeatAvailabilityResponse(bookedSeats, availableSeats);
    }

    private List<String> generateSeats(Integer capacity) {
        List<String> seats = new ArrayList<>();
        char row = 'A';
        for (int i = 1; i <= capacity; i++) {
            seats.add(String.valueOf(row) + i);
            if (i % 10 == 0) {
                row++;
            }
        }
        return seats;
    }

    private ShowResponse toResponse(Show show) {
        if (show == null || show.getMovie() == null || show.getTheatre() == null) {
            return new ShowResponse();
        }
        return new ShowResponse(
                show.getId(),
                show.getMovie().getId(),
                show.getMovie().getTitle(),
                show.getTheatre().getId(),
                show.getTheatre().getName(),
                show.getShowDate(),
                show.getShowTime(),
                show.getTicketPrice(),
                show.getStatus()
        );
    }
}
