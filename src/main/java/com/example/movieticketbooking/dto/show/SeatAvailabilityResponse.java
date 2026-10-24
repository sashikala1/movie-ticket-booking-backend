package com.example.movieticketbooking.dto.show;

import java.util.List;

public class SeatAvailabilityResponse {
    private List<String> bookedSeats;
    private List<String> availableSeats;

    public SeatAvailabilityResponse() {
    }

    public SeatAvailabilityResponse(List<String> bookedSeats, List<String> availableSeats) {
        this.bookedSeats = bookedSeats;
        this.availableSeats = availableSeats;
    }

    public List<String> getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(List<String> bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public List<String> getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(List<String> availableSeats) {
        this.availableSeats = availableSeats;
    }
}
