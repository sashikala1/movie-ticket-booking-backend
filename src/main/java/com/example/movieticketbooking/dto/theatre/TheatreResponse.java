package com.example.movieticketbooking.dto.theatre;

import com.example.movieticketbooking.enums.TheatreStatus;

public class TheatreResponse {
    private Long id;
    private String name;
    private String location;
    private Integer capacity;
    private TheatreStatus status;

    public TheatreResponse() {
    }

    public TheatreResponse(Long id, String name, String location, Integer capacity, TheatreStatus status) {
        this.id = id;
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
}
