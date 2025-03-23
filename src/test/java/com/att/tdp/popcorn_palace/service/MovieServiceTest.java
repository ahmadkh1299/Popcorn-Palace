package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.MovieDTO.AddMovieDTO;
import com.att.tdp.popcorn_palace.dto.MovieDTO.UpdateMovieDTO;
import com.att.tdp.popcorn_palace.entities.Movie;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllMovies() {
        when(movieRepository.findAll()).thenReturn(Collections.emptyList());

        movieService.getAllMovies();

        verify(movieRepository, times(1)).findAll();
    }

    @Test
    public void testGetMovieByTitle() {
        Movie movie = new Movie();
        movie.setTitle("Inception");
        when(movieRepository.findByTitle(anyString())).thenReturn(Optional.of(movie));

        movieService.getMovieByTitle("Inception");

        verify(movieRepository, times(1)).findByTitle(anyString());
    }

    @Test
    public void testAddMovie() {
        AddMovieDTO dto = new AddMovieDTO(
                "Matrix",
                List.of("Action"),
                120,
                8.7,
                1999
        );

        movieService.addMovie(dto);

        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    public void testUpdateMovie() {
        Movie movie = new Movie();
        movie.setTitle("Matrix");
        when(movieRepository.findByTitle(anyString())).thenReturn(Optional.of(movie));

        UpdateMovieDTO dto = new UpdateMovieDTO(
                "Matrix",
                List.of("Sci-Fi"),
                130,
                9.0,
                2000
        );

        movieService.updateMovie("Matrix", dto);

        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    public void testDeleteMovie() {
        Movie movie = new Movie();
        movie.setTitle("Matrix");
        when(movieRepository.findByTitle(anyString())).thenReturn(Optional.of(movie));

        movieService.deleteMovie("Matrix");

        verify(movieRepository, times(1)).delete(movie);
    }
}
