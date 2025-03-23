package com.att.tdp.popcorn_palace.dto.BookingDTO;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class AddBookingDTO {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Seat number is required")
    private Integer seatNumber;

    @NotNull(message = "Showtime ID is required")
    private Long showtimeId;

    // Constructors
    public AddBookingDTO() {
    }

    public AddBookingDTO(UUID userId, Integer seatNumber, Long showtimeId) {
        this.userId = userId;
        this.seatNumber = seatNumber;
        this.showtimeId = showtimeId;
    }

    // Getters and Setters
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Long getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(Long showtimeId) {
        this.showtimeId = showtimeId;
    }
}
