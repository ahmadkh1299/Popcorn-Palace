package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    Optional<Booking> findByShowtimeIdAndSeatNumber(Long showtimeId, int seatNumber);
    boolean existsByShowtimeIdAndSeatNumber(Long showtimeId, int seatNumber);
}
