package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.ShowtimeDTO.AddShowtimeDTO;
import com.att.tdp.popcorn_palace.dto.ShowtimeDTO.GetShowtimeDTO;
import com.att.tdp.popcorn_palace.dto.ShowtimeDTO.UpdateShowtimeDTO;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing showtimes.
 * Provides endpoints to create, retrieve, update, and delete showtimes.
 */
@RestController
@RequestMapping("/showtimes")
@AllArgsConstructor
public class ShowtimeController {

    // Injected service for showtime-related business logic
    private final ShowtimeService showtimeService;

    /**
     * Retrieve a specific showtime by its ID.
     *
     * @param id The ID of the showtime to fetch
     * @return ResponseEntity containing the showtime DTO and HTTP 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<GetShowtimeDTO> getShowtime(@PathVariable Long id) {
        return new ResponseEntity<>(showtimeService.getShowtimeById(id), HttpStatus.OK);
    }

    /**
     * Retrieve all available showtimes.
     *
     * @return ResponseEntity containing the list of showtime DTOs and HTTP 200 OK
     */
    @GetMapping
    public ResponseEntity<List<GetShowtimeDTO>> getAllShowtimes() {
        return new ResponseEntity<>(showtimeService.getAllShowtimes(), HttpStatus.OK);
    }

    /**
     * Add a new showtime.
     * Validates the input and delegates the creation logic to the service layer.
     *
     * @param dto The showtime data to add
     * @return ResponseEntity with HTTP 201 CREATED on success
     */
    @PostMapping
    public ResponseEntity<Void> addShowtime(@Valid @RequestBody AddShowtimeDTO dto) {
        showtimeService.addShowtime(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Update an existing showtime by ID.
     * Checks for overlapping or invalid changes in the service layer.
     *
     * @param id  The ID of the showtime to update
     * @param dto The new showtime data
     * @return ResponseEntity with HTTP 200 OK on success
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateShowtime(@PathVariable Long id, @Valid @RequestBody UpdateShowtimeDTO dto) {
        showtimeService.updateShowtime(id, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Delete a showtime by ID.
     *
     * @param id The ID of the showtime to delete
     * @return ResponseEntity with HTTP 204 NO CONTENT on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        showtimeService.deleteShowtime(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
