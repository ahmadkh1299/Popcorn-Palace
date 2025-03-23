package com.att.tdp.popcorn_palace.dto.BookingDTO;

import java.util.UUID;

public class BookingResponseDTO {

    private UUID bookingId;

    // No-args constructor
    public BookingResponseDTO() {
    }

    // All-args constructor
    public BookingResponseDTO(UUID bookingId) {
        this.bookingId = bookingId;
    }

    // Getter
    public UUID getBookingId() {
        return bookingId;
    }

    // Setter
    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }
}
