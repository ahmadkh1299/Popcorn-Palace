package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.MovieDTO.AddMovieDTO;
import com.att.tdp.popcorn_palace.dto.MovieDTO.GetMovieDTO;
import com.att.tdp.popcorn_palace.dto.MovieDTO.UpdateMovieDTO;
import com.att.tdp.popcorn_palace.entities.Movie;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<GetMovieDTO> getAllMovies() {
        logger.info("Fetching all movies...");
        try {
            return movieRepository.findAll()
                    .stream()
                    .map(this::convertToGetMovieDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching all movies", e);
            throw new RuntimeException("Failed to retrieve movies", e);
        }
    }

    public GetMovieDTO getMovieByTitle(String title) {
        logger.info("Fetching movie by title: {}", title);
        try {
            Movie movie = movieRepository.findByTitle(title)
                    .orElseThrow(() -> new ResourceNotFoundException("Movie not found with title: " + title));
            return convertToGetMovieDTO(movie);
        } catch (Exception e) {
            logger.error("Error fetching movie with title: {}", title, e);
            throw e;
        }
    }

    public void addMovie(AddMovieDTO dto) {
        logger.info("Adding movie: {}", dto.getTitle());
        try {
            Movie movie = new Movie();
            movie.setTitle(dto.getTitle());
            movie.setGenre(dto.getGenre());
            movie.setDuration(dto.getDuration());
            movie.setRating(dto.getRating());
            movie.setReleaseYear(dto.getReleaseYear());
            movieRepository.save(movie);
            logger.info("Movie saved successfully: {}", movie.getTitle());
        } catch (Exception e) {
            logger.error("Error adding movie: {}", dto.getTitle(), e);
            throw new RuntimeException("Failed to add movie", e);
        }
    }

    public void updateMovie(String title, UpdateMovieDTO dto) {
        logger.info("Updating movie with title: {}", title);
        try {
            Movie existingMovie = movieRepository.findByTitle(title)
                    .orElseThrow(() -> new ResourceNotFoundException("Movie not found with title: " + title));

            existingMovie.setTitle(dto.getTitle());
            existingMovie.setGenre(dto.getGenre());
            existingMovie.setDuration(dto.getDuration());
            existingMovie.setRating(dto.getRating());
            existingMovie.setReleaseYear(dto.getReleaseYear());

            movieRepository.save(existingMovie);
            logger.info("Movie updated: {}", existingMovie.getTitle());
        } catch (Exception e) {
            logger.error("Error updating movie with title: {}", title, e);
            throw new RuntimeException("Failed to update movie", e);
        }
    }

    public void deleteMovie(String title) {
        logger.info("Deleting movie with title: {}", title);
        try {
            Movie movie = movieRepository.findByTitle(title)
                    .orElseThrow(() -> new ResourceNotFoundException("Movie not found with title: " + title));
            movieRepository.delete(movie);
            logger.info("Movie deleted: {}", title);
        } catch (Exception e) {
            logger.error("Error deleting movie with title: {}", title, e);
            throw new RuntimeException("Failed to delete movie", e);
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
