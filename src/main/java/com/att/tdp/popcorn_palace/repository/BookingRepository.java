package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Booking entity.
 * Extends JpaRepository to provide basic CRUD operations.
 */
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    /**
     * Retrieves a booking for a specific showtime and seat number.
     *
     * @param showtimeId The ID of the showtime
     * @param seatNumber The seat number
     * @return Optional containing the booking if it exists
     */
    Optional<Booking> findByShowtimeIdAndSeatNumber(Long showtimeId, int seatNumber);

    /**
     * Checks if a booking already exists for a given showtime and seat number.
     *
     * @param showtimeId The ID of the showtime
     * @param seatNumber The seat number
     * @return true if a booking exists; false otherwise
     */
    boolean existsByShowtimeIdAndSeatNumber(Long showtimeId, int seatNumber);
}
