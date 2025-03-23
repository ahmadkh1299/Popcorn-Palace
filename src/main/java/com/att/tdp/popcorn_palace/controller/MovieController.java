package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.MovieDTO.AddMovieDTO;
import com.att.tdp.popcorn_palace.dto.MovieDTO.UpdateMovieDTO;
import com.att.tdp.popcorn_palace.dto.MovieDTO.GetMovieDTO;
import com.att.tdp.popcorn_palace.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<GetMovieDTO>> getAllMovies() {
        List<GetMovieDTO> movies = movieService.getAllMovies();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/{title}")
    public ResponseEntity<GetMovieDTO> getMovieByTitle(@PathVariable String title) {
        GetMovieDTO movie = movieService.getMovieByTitle(title);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addMovie(@Valid @RequestBody AddMovieDTO movieDTO) {
        movieService.addMovie(movieDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{title}")
    public ResponseEntity<Void> updateMovie(@PathVariable String title, @Valid @RequestBody UpdateMovieDTO movieDTO) {
        movieService.updateMovie(title, movieDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<Void> deleteMovie(@PathVariable String title) {
        movieService.deleteMovie(title);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
