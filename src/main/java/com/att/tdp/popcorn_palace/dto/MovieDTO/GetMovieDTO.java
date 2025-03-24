package com.att.tdp.popcorn_palace.dto.MovieDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetMovieDTO {
    private Long id;
    private String title;
    private List<String> genre;
    private int duration;
    private double rating;
    private int releaseYear;
}
