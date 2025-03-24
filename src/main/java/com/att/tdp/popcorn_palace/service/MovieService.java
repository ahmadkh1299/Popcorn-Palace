package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.MovieDTO.AddMovieDTO;
import com.att.tdp.popcorn_palace.dto.MovieDTO.UpdateMovieDTO;
import com.att.tdp.popcorn_palace.dto.MovieDTO.GetMovieDTO;
import com.att.tdp.popcorn_palace.entities.Movie;
import com.att.tdp.popcorn_palace.exception.BadRequestException;
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
        try {
            return movieRepository.findAll()
                    .stream()
                    .map(this::convertToGetMovieDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BadRequestException("Failed to fetch movies from database.");
        }
    }

    public GetMovieDTO getMovieByTitle(String title) {
        try {
            Movie movie = movieRepository.findByTitle(title)
                    .orElseThrow(() -> new ResourceNotFoundException("Movie not found with title: " + title));
            return convertToGetMovieDTO(movie);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Error occurred while retrieving movie with title: " + title);
        }
    }

    public void addMovie(AddMovieDTO dto) {
        try {
            Movie movie = new Movie();
            movie.setTitle(dto.getTitle());
            movie.setGenre(dto.getGenre());
            movie.setDuration(dto.getDuration());
            movie.setRating(dto.getRating());
            movie.setReleaseYear(dto.getReleaseYear());
            movieRepository.save(movie);
        } catch (Exception e) {
            throw new BadRequestException("Failed to add movie. Please try again.");
        }
    }

    public void updateMovie(String title, UpdateMovieDTO dto) {
        try {
            Movie existingMovie = movieRepository.findByTitle(title)
                    .orElseThrow(() -> new ResourceNotFoundException("Movie not found with title: " + title));

            existingMovie.setTitle(dto.getTitle());
            existingMovie.setGenre(dto.getGenre());
            existingMovie.setDuration(dto.getDuration());
            existingMovie.setRating(dto.getRating());
            existingMovie.setReleaseYear(dto.getReleaseYear());

            movieRepository.save(existingMovie);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to update movie.");
        }
    }

    public void deleteMovie(String title) {
        try {
            Movie movie = movieRepository.findByTitle(title)
                    .orElseThrow(() -> new ResourceNotFoundException("Movie not found with title: " + title));
            movieRepository.delete(movie);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to delete movie.");
        }
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
