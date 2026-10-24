package com.example.movieticketbooking.dto.movie;

import com.example.movieticketbooking.enums.MovieStatus;

import java.time.LocalDate;

public class MovieResponse {
    private Long id;
    private String title;
    private String description;
    private Integer duration;
    private String language;
    private String genre;
    private LocalDate releaseDate;
    private MovieStatus status;

    public MovieResponse() {
    }

    public MovieResponse(Long id, String title, String description, Integer duration, String language, String genre, LocalDate releaseDate, MovieStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.language = language;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public MovieStatus getStatus() {
        return status;
    }

    public void setStatus(MovieStatus status) {
        this.status = status;
    }
}
