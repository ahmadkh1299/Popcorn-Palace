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

@RestController
@RequestMapping("/movies")
@AllArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<List<GetMovieDTO>> getAllMovies() {
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }

    @GetMapping("/{title}")
    public ResponseEntity<GetMovieDTO> getMovieByTitle(@PathVariable String title) {
        return new ResponseEntity<>(movieService.getMovieByTitle(title), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addMovie(@Valid @RequestBody AddMovieDTO dto) {
        Long id = movieService.addMovie(dto);
        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping("/{title}")
    public ResponseEntity<Void> updateMovie(@PathVariable String title, @Valid @RequestBody UpdateMovieDTO movie) {
        movieService.updateMovie(title, movie);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<Void> deleteMovie(@PathVariable String title) {
        movieService.deleteMovie(title);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
