package com.example.movieticketbooking.dto.show;

import com.example.movieticketbooking.enums.ShowStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class ShowResponse {
    private Long id;
    private Long movieId;
    private String movieTitle;
    private Long theatreId;
    private String theatreName;
    private LocalDate showDate;
    private LocalTime showTime;
    private BigDecimal ticketPrice;
    private ShowStatus status;

    public ShowResponse() {
    }

    public ShowResponse(Long id, Long movieId, String movieTitle, Long theatreId, String theatreName, LocalDate showDate, LocalTime showTime, BigDecimal ticketPrice, ShowStatus status) {
        this.id = id;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.theatreId = theatreId;
        this.theatreName = theatreName;
        this.showDate = showDate;
        this.showTime = showTime;
        this.ticketPrice = ticketPrice;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public Long getTheatreId() {
        return theatreId;
    }

    public void setTheatreId(Long theatreId) {
        this.theatreId = theatreId;
    }

    public String getTheatreName() {
        return theatreName;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
    }

    public LocalDate getShowDate() {
        return showDate;
    }

    public void setShowDate(LocalDate showDate) {
        this.showDate = showDate;
    }

    public LocalTime getShowTime() {
        return showTime;
    }

    public void setShowTime(LocalTime showTime) {
        this.showTime = showTime;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public ShowStatus getStatus() {
        return status;
    }

    public void setStatus(ShowStatus status) {
        this.status = status;
    }
}
