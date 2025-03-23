package com.att.tdp.popcorn_palace.dto.MovieDTO;

import jakarta.validation.constraints.*;

import java.util.List;

public class AddMovieDTO {

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

    public AddMovieDTO() {}

    public AddMovieDTO(String title, List<String> genre, int duration, double rating, int releaseYear) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.releaseYear = releaseYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }
}
