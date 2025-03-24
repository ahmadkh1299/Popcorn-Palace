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

    public BookingResponseDTO bookTicket(AddBookingDTO dto) {
        logger.info("Attempting to book a ticket: {}", dto);

        Showtime showtime;
        try {
            showtime = showtimeRepository.findById(dto.getShowtimeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + dto.getShowtimeId()));
        } catch (Exception e) {
            logger.error("Failed to fetch showtime with id: {}", dto.getShowtimeId(), e);
            throw e;
        }

        boolean seatAlreadyBooked;
        try {
            seatAlreadyBooked = bookingRepository.existsByShowtimeIdAndSeatNumber(dto.getShowtimeId(), dto.getSeatNumber());
        } catch (Exception e) {
            logger.error("Failed to check seat availability", e);
            throw new RuntimeException("Error checking seat availability", e);
        }

        if (seatAlreadyBooked) {
            logger.warn("Seat already booked: seatNumber={} for showtimeId={}", dto.getSeatNumber(), dto.getShowtimeId());
            throw new BadRequestException("Seat " + dto.getSeatNumber() + " is already booked for this showtime.");
        }

        Booking booking = new Booking();
        booking.setBookingId(UUID.randomUUID());
        booking.setUserId(dto.getUserId());
        booking.setSeatNumber(dto.getSeatNumber());
        booking.setShowtime(showtime);

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
