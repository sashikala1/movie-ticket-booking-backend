package com.example.movieticketbooking.dto.booking;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class BookingRequest {

    @NotNull(message = "Show ID is required")
    private Long showId;

    @NotEmpty(message = "At least one seat is required")
    private List<String> seatNumbers;

    @NotNull(message = "Number of tickets is required")
    @Min(value = 1, message = "Number of tickets must be at least 1")
    private Integer numberOfTickets;

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public List<String> getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(List<String> seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public Integer getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(Integer numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }
}
