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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowtimeService {

    private static final Logger logger = LoggerFactory.getLogger(ShowtimeService.class);

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;

    public ShowtimeService(ShowtimeRepository showtimeRepository, MovieRepository movieRepository) {
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
    }

    /**
     * Fetch a showtime by its ID.
     *
     * @param id the ID of the showtime
     * @return the corresponding DTO
     */
    public GetShowtimeDTO getShowtimeById(Long id) {
        logger.info("Fetching showtime by ID: {}", id);
        try {
            Showtime showtime = showtimeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + id));
            return convertToDTO(showtime);
        } catch (Exception e) {
            logger.error("Error fetching showtime with ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Fetch all showtimes in the system.
     *
     * @return list of all showtimes as DTOs
     */
    public List<GetShowtimeDTO> getAllShowtimes() {
        logger.info("Fetching all showtimes...");
        try {
            return showtimeRepository.findAll()
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching all showtimes", e);
            throw new RuntimeException("Failed to retrieve showtimes", e);
        }
    }

    /**
     * Add a new showtime if it does not overlap with existing ones in the same theater.
     *
     * @param dto the showtime to add
     */
    public void addShowtime(AddShowtimeDTO dto) {
        logger.info("Adding new showtime for theater: {}", dto.getTheater());

        // 1. Make sure the movie exists
        Movie movie;
        try {
            movie = movieRepository.findById(dto.getMovieId())
                    .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + dto.getMovieId()));
        } catch (Exception e) {
            logger.error("Error retrieving movie for showtime", e);
            throw e;
        }

        try {
            // 2. Check for overlapping showtimes in the same theater
            List<Showtime> overlapping = showtimeRepository.findOverlappingShowtimes(
                    dto.getTheater(), dto.getStartTime(), dto.getEndTime()
            );

            if (!overlapping.isEmpty()) {
                logger.warn("Overlapping showtime found for theater: {}", dto.getTheater());
                throw new BadRequestException("Overlapping showtime already exists for theater: " + dto.getTheater());
            }

            // 3. Save the new showtime
            Showtime showtime = new Showtime();
            showtime.setMovie(movie);
            showtime.setPrice(dto.getPrice());
            showtime.setTheater(dto.getTheater());
            showtime.setStartTime(dto.getStartTime());
            showtime.setEndTime(dto.getEndTime());

            showtimeRepository.save(showtime);
            logger.info("Showtime saved successfully");

        } catch (Exception e) {
            logger.error("Error while adding showtime", e);
            throw e;
        }
    }

    /**
     * Update an existing showtime, making sure the new time does not overlap with others.
     *
     * @param id  the ID of the showtime to update
     * @param dto new showtime values
     */
    public void updateShowtime(Long id, UpdateShowtimeDTO dto) {
        logger.info("Updating showtime with ID: {}", id);
        try {
            // 1. Ensure showtime exists
            Showtime existing = showtimeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + id));

            // 2. Ensure new movie exists
            Movie movie = movieRepository.findById(dto.getMovieId())
                    .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + dto.getMovieId()));

            // 3. Find overlapping showtimes in the same theater, ignoring this one
            List<Showtime> overlapping = showtimeRepository.findOverlappingShowtimes(
                            dto.getTheater(), dto.getStartTime(), dto.getEndTime()
                    ).stream()
                    .filter(s -> !s.getId().equals(id)) // exclude the current one
                    .collect(Collectors.toList());

            if (!overlapping.isEmpty()) {
                logger.warn("Update overlaps with existing showtime in theater: {}", dto.getTheater());
                throw new BadRequestException("Updated showtime overlaps with another showtime in theater: " + dto.getTheater());
            }

            // 4. Apply changes and save
            existing.setMovie(movie);
            existing.setPrice(dto.getPrice());
            existing.setTheater(dto.getTheater());
            existing.setStartTime(dto.getStartTime());
            existing.setEndTime(dto.getEndTime());

            showtimeRepository.save(existing);
            logger.info("Showtime updated successfully for ID: {}", id);

        } catch (BadRequestException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error updating showtime with ID: {}", id, e);
            throw new RuntimeException("Failed to update showtime", e);
        }
    }

    /**
     * Delete a showtime by its ID.
     *
     * @param id the ID of the showtime to delete
     */
    public void deleteShowtime(Long id) {
        logger.info("Deleting showtime with ID: {}", id);
        try {
            if (!showtimeRepository.existsById(id)) {
                throw new ResourceNotFoundException("Showtime not found with id: " + id);
            }
            showtimeRepository.deleteById(id);
            logger.info("Showtime deleted with ID: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting showtime with ID: {}", id, e);
            throw new RuntimeException("Failed to delete showtime", e);
        }
    }

    /**
     * Convert a Showtime entity to its corresponding DTO.
     *
     * @param showtime the entity
     * @return the DTO
     */
    private GetShowtimeDTO convertToDTO(Showtime showtime) {
        if (showtime.getMovie() == null) {
            logger.error("Showtime is missing movie reference!");
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
