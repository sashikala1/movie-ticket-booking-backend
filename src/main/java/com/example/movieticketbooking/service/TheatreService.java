package com.example.movieticketbooking.service;

import com.example.movieticketbooking.dto.theatre.TheatreRequest;
import com.example.movieticketbooking.dto.theatre.TheatreResponse;
import com.example.movieticketbooking.entity.Theatre;
import com.example.movieticketbooking.enums.TheatreStatus;
import com.example.movieticketbooking.exception.BadRequestException;
import com.example.movieticketbooking.exception.ConflictException;
import com.example.movieticketbooking.exception.ResourceNotFoundException;
import com.example.movieticketbooking.repository.TheatreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheatreService {

    private static final Logger log = LoggerFactory.getLogger(TheatreService.class);

    private final TheatreRepository theatreRepository;

    public TheatreService(TheatreRepository theatreRepository) {
        this.theatreRepository = theatreRepository;
    }

    public TheatreResponse createTheatre(TheatreRequest request) {
        Theatre theatre = new Theatre(
                request.getName(),
                request.getLocation(),
                request.getCapacity(),
                request.getStatus()
        );

        Theatre saved = theatreRepository.save(theatre);
        log.info("Theatre created: {}", saved.getName());
        return toResponse(saved);
    }

    public List<TheatreResponse> getAllTheatres() {
        return theatreRepository.findAll().stream().map(this::toResponse).toList();
    }

    public TheatreResponse getTheatreById(Long id) {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + id));
        return toResponse(theatre);
    }

    public TheatreResponse updateTheatre(Long id, TheatreRequest request) {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + id));

        theatre.setName(request.getName());
        theatre.setLocation(request.getLocation());
        theatre.setCapacity(request.getCapacity());
        theatre.setStatus(request.getStatus());

        Theatre saved = theatreRepository.save(theatre);
        log.info("Theatre updated: {}", saved.getName());
        return toResponse(saved);
    }

    public void deleteTheatre(Long id) {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + id));
        theatreRepository.delete(theatre);
        log.info("Theatre deleted: {}", theatre.getName());
    }

    public void validateTheatreActive(Long theatreId) {
        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + theatreId));
        if (!TheatreStatus.ACTIVE.equals(theatre.getStatus())) {
            throw new ConflictException("Inactive theatre cannot receive a new show");
        }
    }

    private TheatreResponse toResponse(Theatre theatre) {
        return new TheatreResponse(
                theatre.getId(),
                theatre.getName(),
                theatre.getLocation(),
                theatre.getCapacity(),
                theatre.getStatus()
        );
    }
}
