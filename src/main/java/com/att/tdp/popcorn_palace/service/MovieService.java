package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.MovieDTO.AddMovieDTO;
import com.att.tdp.popcorn_palace.dto.MovieDTO.UpdateMovieDTO;
import com.att.tdp.popcorn_palace.dto.MovieDTO.GetMovieDTO;
import com.att.tdp.popcorn_palace.entities.Movie;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<GetMovieDTO> getAllMovies() {
        return movieRepository.findAll()
                .stream()
                .map(this::convertToGetMovieDTO)
                .collect(Collectors.toList());
    }

    public GetMovieDTO getMovieByTitle(String title) {
        Movie movie = movieRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with title: " + title));
        return convertToGetMovieDTO(movie);
    }

    public void addMovie(AddMovieDTO dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setGenre(dto.getGenre());
        movie.setDuration(dto.getDuration());
        movie.setRating(dto.getRating());
        movie.setReleaseYear(dto.getReleaseYear());
        movieRepository.save(movie);
    }

    public void updateMovie(String title, UpdateMovieDTO dto) {
        Movie existingMovie = movieRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with title: " + title));

        existingMovie.setTitle(dto.getTitle());
        existingMovie.setGenre(dto.getGenre());
        existingMovie.setDuration(dto.getDuration());
        existingMovie.setRating(dto.getRating());
        existingMovie.setReleaseYear(dto.getReleaseYear());

        movieRepository.save(existingMovie);
    }

    public void deleteMovie(String title) {
        Movie movie = movieRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with title: " + title));
        movieRepository.delete(movie);
    }

    private GetMovieDTO convertToGetMovieDTO(Movie movie) {
        return new GetMovieDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getGenre(),
                movie.getDuration(),
                movie.getRating(),
                movie.getReleaseYear()
        );
    }
}
