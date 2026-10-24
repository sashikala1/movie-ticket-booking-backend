package com.example.movieticketbooking.repository;

import com.example.movieticketbooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);

    List<Booking> findByShowId(Long showId);

    @Query("SELECT b FROM Booking b WHERE b.show.id = :showId AND b.status <> com.example.movieticketbooking.enums.BookingStatus.CANCELLED")
    List<Booking> findActiveByShowId(@Param("showId") Long showId);
}
