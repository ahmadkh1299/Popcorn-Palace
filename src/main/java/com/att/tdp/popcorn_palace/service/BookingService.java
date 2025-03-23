package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.BookingDTO.AddBookingDTO;
import com.att.tdp.popcorn_palace.dto.BookingDTO.BookingResponseDTO;
import com.att.tdp.popcorn_palace.entities.Booking;
import com.att.tdp.popcorn_palace.entities.Showtime;
import com.att.tdp.popcorn_palace.exception.BadRequestException;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, ShowtimeRepository showtimeRepository) {
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
    }

    public BookingResponseDTO bookTicket(AddBookingDTO dto) {
        Showtime showtime = showtimeRepository.findById(dto.getShowtimeId())
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + dto.getShowtimeId()));

        boolean seatAlreadyBooked = bookingRepository.existsByShowtimeIdAndSeatNumber(dto.getShowtimeId(), dto.getSeatNumber());
        if (seatAlreadyBooked) {
            throw new BadRequestException("Seat " + dto.getSeatNumber() + " is already booked for this showtime.");
        }

        Booking booking = new Booking();
        booking.setBookingId(UUID.randomUUID());
        booking.setUserId(dto.getUserId());
        booking.setSeatNumber(dto.getSeatNumber());
        booking.setShowtime(showtime);

        Booking savedBooking = bookingRepository.save(booking);
        return new BookingResponseDTO(savedBooking.getBookingId());
    }
}
