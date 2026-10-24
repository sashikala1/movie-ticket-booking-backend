package com.example.movieticketbooking.dto.booking;

import com.example.movieticketbooking.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BookingResponse {
    private Long id;
    private Long userId;
    private Long showId;
    private String movieTitle;
    private String theatreName;
    private List<String> seatNumbers;
    private Integer numberOfTickets;
    private BigDecimal totalAmount;
    private LocalDateTime bookingDate;
    private BookingStatus status;

    public BookingResponse() {
    }

    public BookingResponse(Long id, Long userId, Long showId, String movieTitle, String theatreName, List<String> seatNumbers, Integer numberOfTickets, BigDecimal totalAmount, LocalDateTime bookingDate, BookingStatus status) {
        this.id = id;
        this.userId = userId;
        this.showId = showId;
        this.movieTitle = movieTitle;
        this.theatreName = theatreName;
        this.seatNumbers = seatNumbers;
        this.numberOfTickets = numberOfTickets;
        this.totalAmount = totalAmount;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getTheatreName() {
        return theatreName;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
