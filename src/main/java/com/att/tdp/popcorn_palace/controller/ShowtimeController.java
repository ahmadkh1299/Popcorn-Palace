package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.ShowtimeDTO.AddShowtimeDTO;
import com.att.tdp.popcorn_palace.dto.ShowtimeDTO.UpdateShowtimeDTO;
import com.att.tdp.popcorn_palace.dto.ShowtimeDTO.GetShowtimeDTO;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    @Autowired
    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetShowtimeDTO> getShowtime(@PathVariable Long id) {
        return new ResponseEntity<>(showtimeService.getShowtimeById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<GetShowtimeDTO>> getAllShowtimes() {
        return new ResponseEntity<>(showtimeService.getAllShowtimes(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addShowtime(@Valid @RequestBody AddShowtimeDTO dto) {
        showtimeService.addShowtime(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateShowtime(@PathVariable Long id, @Valid @RequestBody UpdateShowtimeDTO dto) {
        showtimeService.updateShowtime(id, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        showtimeService.deleteShowtime(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
