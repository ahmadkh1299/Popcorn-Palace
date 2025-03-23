package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.entities.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    /**
     * Finds all showtimes that overlap with the given time range for a specific theater.
     *
     * An overlap exists if:
     *   existing.start < new.end AND existing.end > new.start
     */
    @Query("SELECT s FROM Showtime s WHERE s.theater = :theater AND " +
            "s.startTime < :endTime AND s.endTime > :startTime")
    List<Showtime> findOverlappingShowtimes(
            @Param("theater") String theater,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
