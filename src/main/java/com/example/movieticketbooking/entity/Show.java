package com.example.movieticketbooking.entity;

import com.example.movieticketbooking.enums.ShowStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shows")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "theatre_id", nullable = false)
    private Theatre theatre;

    @Column(name = "show_date", nullable = false)
    private LocalDate showDate;

    @Column(name = "show_time", nullable = false)
    private LocalTime showTime;

    @Column(name = "ticket_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal ticketPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShowStatus status;

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    public Show() {
    }

    public Show(Movie movie, Theatre theatre, LocalDate showDate, LocalTime showTime, BigDecimal ticketPrice, ShowStatus status) {
        this.movie = movie;
        this.theatre = theatre;
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

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Theatre getTheatre() {
        return theatre;
    }

    public void setTheatre(Theatre theatre) {
        this.theatre = theatre;
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

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
