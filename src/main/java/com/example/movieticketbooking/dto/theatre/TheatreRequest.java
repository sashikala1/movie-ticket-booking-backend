package com.example.movieticketbooking.dto.theatre;

import com.example.movieticketbooking.enums.TheatreStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TheatreRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be positive")
    private Integer capacity;

    @NotNull(message = "Status is required")
    private TheatreStatus status;

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
