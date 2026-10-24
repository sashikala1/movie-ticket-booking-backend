package com.example.movieticketbooking.entity;

import com.example.movieticketbooking.enums.TheatreStatus;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "theatres")
public class Theatre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TheatreStatus status;

    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Show> shows = new ArrayList<>();

    public Theatre() {
    }

    public Theatre(String name, String location, Integer capacity, TheatreStatus status) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public TheatreStatus getStatus() {
        return status;
    }

    public void setStatus(TheatreStatus status) {
        this.status = status;
    }

    public List<Show> getShows() {
        return shows;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }
}
