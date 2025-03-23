package com.att.tdp.popcorn_palace.dto.MovieDTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMovieDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotEmpty(message = "Genre must not be empty")
    private List<@NotBlank String> genre;

    @Positive(message = "Duration must be greater than zero")
    private int duration;

    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "10.0", inclusive = true)
    private double rating;

    @Min(value = 1888, message = "Release year is invalid")
    private int releaseYear;
}
