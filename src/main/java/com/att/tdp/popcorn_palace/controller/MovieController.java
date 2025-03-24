package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.MovieDTO.AddMovieDTO;
import com.att.tdp.popcorn_palace.dto.MovieDTO.GetMovieDTO;
import com.att.tdp.popcorn_palace.dto.MovieDTO.UpdateMovieDTO;
import com.att.tdp.popcorn_palace.service.MovieService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for handling movie-related operations such as
 * fetching, adding, updating, and deleting movies.
 */
@RestController
@RequestMapping("/movies")
@AllArgsConstructor
public class MovieController {

    // The service that contains all business logic related to movies
    private final MovieService movieService;

    /**
     * Retrieves a list of all movies.
     *
     * @return ResponseEntity containing the list of all movies and HTTP 200 OK
     */
    @GetMapping
    public ResponseEntity<List<GetMovieDTO>> getAllMovies() {
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }

    /**
     * Retrieves a movie by its title.
     *
     * @param title the title of the movie
     * @return ResponseEntity containing the movie DTO and HTTP 200 OK
     */
    @GetMapping("/{title}")
    public ResponseEntity<GetMovieDTO> getMovieByTitle(@PathVariable String title) {
        return new ResponseEntity<>(movieService.getMovieByTitle(title), HttpStatus.OK);
    }

    /**
     * Adds a new movie to the system.
     *
     * @param dto the movie details to add
     * @return ResponseEntity with the generated movie ID and HTTP 201 CREATED
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> addMovie(@Valid @RequestBody AddMovieDTO dto) {
        Long id = movieService.addMovie(dto);
        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Updates an existing movie by title.
     *
     * @param title the title of the movie to update
     * @param movie the updated movie details
     * @return ResponseEntity with HTTP 200 OK on success
     */
    @PutMapping("/{title}")
    public ResponseEntity<Void> updateMovie(@PathVariable String title, @Valid @RequestBody UpdateMovieDTO movie) {
        movieService.updateMovie(title, movie);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Deletes a movie by title.
     *
     * @param title the title of the movie to delete
     * @return ResponseEntity with HTTP 204 NO CONTENT on success
     */
    @DeleteMapping("/{title}")
    public ResponseEntity<Void> deleteMovie(@PathVariable String title) {
        movieService.deleteMovie(title);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
