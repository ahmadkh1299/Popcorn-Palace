package com.att.tdp.popcorn_palace.dto.ShowtimeDTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShowtimeDTO {

    @NotNull(message = "Movie ID is required")
    private Long movieId;

    @Positive(message = "Price must be greater than 0")
    private double price;

    @NotBlank(message = "Theater is required")
    private String theater;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;
}
