package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.BookingDTO.AddBookingDTO;
import com.att.tdp.popcorn_palace.dto.BookingDTO.BookingResponseDTO;
import com.att.tdp.popcorn_palace.exception.BadRequestException;
import com.att.tdp.popcorn_palace.service.BookingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> bookTicket(@RequestBody AddBookingDTO request) {
        if (request.getSeatNumber() == null || request.getSeatNumber() < 1) {
            throw new BadRequestException("Seat number must be greater than 0");
        }

        if (request.getUserId() == null) {
            throw new BadRequestException("User ID is required");
        }

        if (request.getShowtimeId() == null) {
            throw new BadRequestException("Showtime ID is required");
        }

        BookingResponseDTO response = bookingService.bookTicket(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
