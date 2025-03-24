package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.ShowtimeDTO.AddShowtimeDTO;
import com.att.tdp.popcorn_palace.dto.ShowtimeDTO.GetShowtimeDTO;
import com.att.tdp.popcorn_palace.dto.ShowtimeDTO.UpdateShowtimeDTO;
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
        try {
            Showtime showtime = showtimeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + id));
            return convertToDTO(showtime);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to retrieve showtime with id: " + id);
        }
    }

    public List<GetShowtimeDTO> getAllShowtimes() {
        try {
            return showtimeRepository.findAll()
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BadRequestException("Failed to retrieve showtimes from the database.");
        }
    }

    public void addShowtime(AddShowtimeDTO dto) {
        try {
            Movie movie = movieRepository.findById(dto.getMovieId())
                    .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + dto.getMovieId()));

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
        } catch (ResourceNotFoundException | BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to add showtime. Please try again.");
        }
    }

    public void updateShowtime(Long id, UpdateShowtimeDTO dto) {
        try {
            Showtime existing = showtimeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + id));

            Movie movie = movieRepository.findById(dto.getMovieId())
                    .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + dto.getMovieId()));

            List<Showtime> overlapping = showtimeRepository.findOverlappingShowtimes(
                    dto.getTheater(), dto.getStartTime(), dto.getEndTime()
            ).stream().filter(s -> !s.getId().equals(id)).collect(Collectors.toList());

            if (!overlapping.isEmpty()) {
                throw new BadRequestException("Updated showtime overlaps with another showtime in theater: " + dto.getTheater());
            }

            existing.setMovie(movie);
            existing.setPrice(dto.getPrice());
            existing.setTheater(dto.getTheater());
            existing.setStartTime(dto.getStartTime());
            existing.setEndTime(dto.getEndTime());

            showtimeRepository.save(existing);
        } catch (ResourceNotFoundException | BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to update showtime.");
        }
    }

    public void deleteShowtime(Long id) {
        try {
            if (!showtimeRepository.existsById(id)) {
                throw new ResourceNotFoundException("Showtime not found with id: " + id);
            }
            showtimeRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to delete showtime.");
        }
    }

    private GetShowtimeDTO convertToDTO(Showtime showtime) {
        if (showtime.getMovie() == null) {
            throw new BadRequestException("Showtime is missing associated movie.");
        }

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
