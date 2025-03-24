package com.att.tdp.popcorn_palace.dto.BookingDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddBookingDTO {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Seat number is required")
    private Integer seatNumber;

    @NotNull(message = "Showtime ID is required")
    private Long showtimeId;
}
