package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.ShowtimeDTO.AddShowtimeDTO;
import com.att.tdp.popcorn_palace.dto.ShowtimeDTO.UpdateShowtimeDTO;
import com.att.tdp.popcorn_palace.dto.ShowtimeDTO.GetShowtimeDTO;
import com.att.tdp.popcorn_palace.entities.Movie;
import com.att.tdp.popcorn_palace.entities.Showtime;
import com.att.tdp.popcorn_palace.exception.BadRequestException;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public ShowtimeService(ShowtimeRepository showtimeRepository, MovieRepository movieRepository) {
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
    }

    public GetShowtimeDTO getShowtimeById(Long id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + id));
        return convertToDTO(showtime);
    }

    public List<GetShowtimeDTO> getAllShowtimes() {
        return showtimeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void addShowtime(AddShowtimeDTO dto) {
        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + dto.getMovieId()));

        //  Check for overlapping showtime
        List<Showtime> overlapping = showtimeRepository.findOverlappingShowtimes(
                dto.getTheater(), dto.getStartTime(), dto.getEndTime()
        );

        if (!overlapping.isEmpty()) {
            throw new BadRequestException("Overlapping showtime already exists for theater: " + dto.getTheater());
        }

        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setPrice(dto.getPrice());
        showtime.setTheater(dto.getTheater());
        showtime.setStartTime(dto.getStartTime());
        showtime.setEndTime(dto.getEndTime());

        showtimeRepository.save(showtime);
    }

    public void updateShowtime(Long id, UpdateShowtimeDTO dto) {
        Showtime existing = showtimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + id));

        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + dto.getMovieId()));

        //  Check for overlapping showtime excluding the current one
        List<Showtime> overlapping = showtimeRepository.findOverlappingShowtimes(
                        dto.getTheater(), dto.getStartTime(), dto.getEndTime()
                ).stream()
                .filter(s -> !s.getId().equals(id))
                .collect(Collectors.toList());

        if (!overlapping.isEmpty()) {
            throw new BadRequestException("Updated showtime overlaps with another showtime in theater: " + dto.getTheater());
        }

        existing.setMovie(movie);
        existing.setPrice(dto.getPrice());
        existing.setTheater(dto.getTheater());
        existing.setStartTime(dto.getStartTime());
        existing.setEndTime(dto.getEndTime());

        showtimeRepository.save(existing);
    }

    public void deleteShowtime(Long id) {
        if (!showtimeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Showtime not found with id: " + id);
        }
        showtimeRepository.deleteById(id);
    }

    private GetShowtimeDTO convertToDTO(Showtime showtime) {
        return new GetShowtimeDTO(
                showtime.getId(),
                showtime.getMovie().getId(),
                showtime.getPrice(),
                showtime.getTheater(),
                showtime.getStartTime(),
                showtime.getEndTime()
        );
    }
}
