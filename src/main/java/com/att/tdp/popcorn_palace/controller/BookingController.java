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

/**
 * REST controller for handling booking-related endpoints.
 */
@RestController
@RequestMapping("/bookings") // Base path for booking-related operations
@AllArgsConstructor
public class BookingController {

    // Booking service that contains the business logic
    private final BookingService bookingService;

    /**
     * Endpoint to create a new booking.
     * Validates the incoming request and delegates to the BookingService.
     *
     * @param request The booking request containing user ID, showtime ID, and seat number.
     * @return A response entity containing the booking confirmation and status CREATED.
     */
    @PostMapping
    public ResponseEntity<BookingResponseDTO> bookTicket(@RequestBody AddBookingDTO request) {
        // Input validations
        if (request.getSeatNumber() == null || request.getSeatNumber() < 1) {
            throw new BadRequestException("Seat number must be greater than 0");
        }

        if (request.getUserId() == null) {
            throw new BadRequestException("User ID is required");
        }

        if (request.getShowtimeId() == null) {
            throw new BadRequestException("Showtime ID is required");
        }

        // Delegate to service layer to perform the booking
        BookingResponseDTO response = bookingService.bookTicket(request);

        // Return the created booking response
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
