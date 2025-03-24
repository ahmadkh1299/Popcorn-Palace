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

    /**
     * Retrieve all movies from the database and convert them to DTOs.
     *
     * @return a list of GetMovieDTO objects
     */
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

    /**
     * Retrieve a single movie by its title.
     *
     * @param title the title of the movie to retrieve
     * @return the movie as a GetMovieDTO
     */
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

    /**
     * Add a new movie to the database.
     *
     * @param dto the data needed to create a movie
     * @return the ID of the newly created movie
     */
    public Long addMovie(AddMovieDTO dto) {
        logger.info("Adding movie: {}", dto.getTitle());
        try {
            Movie movie = new Movie();
            movie.setTitle(dto.getTitle());
            movie.setGenre(dto.getGenre());
            movie.setDuration(dto.getDuration());
            movie.setRating(dto.getRating());
            movie.setReleaseYear(dto.getReleaseYear());

            movieRepository.save(movie);

            logger.info("Movie saved successfully: {} with ID {}", movie.getTitle(), movie.getId());
            return movie.getId();  // Return the auto-generated ID after saving
        } catch (Exception e) {
            logger.error("Error adding movie: {}", dto.getTitle(), e);
            throw new RuntimeException("Failed to add movie", e);
        }
    }

    /**
     * Update an existing movie's details using its title.
     *
     * @param title the title of the movie to update
     * @param dto the new data to apply
     */
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

    /**
     * Delete a movie from the database using its title.
     *
     * @param title the title of the movie to delete
     */
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

    /**
     * Helper method to convert a Movie entity into a GetMovieDTO.
     *
     * @param movie the Movie entity
     * @return the corresponding DTO
     */
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
