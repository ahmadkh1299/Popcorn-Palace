package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.BookingDTO.AddBookingDTO;
import com.att.tdp.popcorn_palace.dto.BookingDTO.BookingResponseDTO;
import com.att.tdp.popcorn_palace.entities.Booking;
import com.att.tdp.popcorn_palace.entities.Showtime;
import com.att.tdp.popcorn_palace.exception.BadRequestException;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;

    public BookingService(BookingRepository bookingRepository, ShowtimeRepository showtimeRepository) {
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
    }

    /**
     * Books a ticket for a specific showtime and seat.
     *
     * @param dto The booking request containing showtime ID, user ID, and seat number
     * @return BookingResponseDTO with booking confirmation (UUID)
     */
    public BookingResponseDTO bookTicket(AddBookingDTO dto) {
        logger.info("Attempting to book a ticket: {}", dto);

        // 1. Fetch the corresponding showtime from the database
        Showtime showtime;
        try {
            showtime = showtimeRepository.findById(dto.getShowtimeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + dto.getShowtimeId()));
        } catch (Exception e) {
            logger.error("Failed to fetch showtime with id: {}", dto.getShowtimeId(), e);
            throw e;
        }

        // 2. Check if the seat is already booked for this showtime
        boolean seatAlreadyBooked;
        try {
            seatAlreadyBooked = bookingRepository.existsByShowtimeIdAndSeatNumber(dto.getShowtimeId(), dto.getSeatNumber());
        } catch (Exception e) {
            logger.error("Failed to check seat availability", e);
            throw new RuntimeException("Error checking seat availability", e);
        }

        // 3. If booked, throw a custom BadRequestException
        if (seatAlreadyBooked) {
            logger.warn("Seat already booked: seatNumber={} for showtimeId={}", dto.getSeatNumber(), dto.getShowtimeId());
            throw new BadRequestException("Seat " + dto.getSeatNumber() + " is already booked for this showtime.");
        }

        // 4. Create a new booking entry
        Booking booking = new Booking();
        booking.setBookingId(UUID.randomUUID()); // Assign a unique booking ID
        booking.setUserId(dto.getUserId());
        booking.setSeatNumber(dto.getSeatNumber());
        booking.setShowtime(showtime); // Link to the showtime entity

        // 5. Save the booking in the database and return confirmation
        try {
            Booking savedBooking = bookingRepository.save(booking);
            logger.info("Booking saved successfully: {}", savedBooking.getBookingId());
            return new BookingResponseDTO(savedBooking.getBookingId());
        } catch (Exception e) {
            logger.error("Failed to save booking", e);
            throw new RuntimeException("Error saving booking", e);
        }
    }
}
