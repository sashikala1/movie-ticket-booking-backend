package com.example.movieticketbooking.repository;

import com.example.movieticketbooking.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByMovieId(Long movieId);

    List<Show> findByTheatreId(Long theatreId);

    @Query("SELECT s FROM Show s WHERE s.theatre.id = :theatreId AND s.showDate = :showDate AND s.showTime = :showTime AND s.status <> com.example.movieticketbooking.enums.ShowStatus.CANCELLED")
    List<Show> findConflictingShows(@Param("theatreId") Long theatreId,
                                   @Param("showDate") LocalDate showDate,
                                   @Param("showTime") LocalTime showTime);
}
