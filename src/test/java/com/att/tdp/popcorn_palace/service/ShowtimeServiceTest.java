package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.ShowtimeDTO.AddShowtimeDTO;
import com.att.tdp.popcorn_palace.dto.ShowtimeDTO.GetShowtimeDTO;
import com.att.tdp.popcorn_palace.dto.ShowtimeDTO.UpdateShowtimeDTO;
import com.att.tdp.popcorn_palace.entities.Movie;
import com.att.tdp.popcorn_palace.entities.Showtime;
import com.att.tdp.popcorn_palace.exception.BadRequestException;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ShowtimeServiceTest {

    @InjectMocks
    private ShowtimeService showtimeService;

    @Mock
    private ShowtimeRepository showtimeRepository;

    @Mock
    private MovieRepository movieRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllShowtimes() {
        when(showtimeRepository.findAll()).thenReturn(Collections.emptyList());

        showtimeService.getAllShowtimes();

        verify(showtimeRepository, times(1)).findAll();
    }

    @Test
    public void testGetShowtimeById() {
        Movie movie = new Movie();
        movie.setId(1L); // ✅ Make sure the ID is set

        Showtime showtime = new Showtime();
        showtime.setId(1L);
        showtime.setMovie(movie); // ✅ Associate movie with showtime
        showtime.setPrice(50.0);
        showtime.setTheater("Theater A");
        showtime.setStartTime(LocalDateTime.now());
        showtime.setEndTime(LocalDateTime.now().plusHours(2));

        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));

        GetShowtimeDTO result = showtimeService.getShowtimeById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Theater A", result.getTheater());
        verify(showtimeRepository, times(1)).findById(1L);
    }


    @Test
    public void testAddShowtime() {
        Movie movie = new Movie();
        movie.setId(1L);
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie));
        when(showtimeRepository.findOverlappingShowtimes(anyString(), any(), any())).thenReturn(Collections.emptyList());

        AddShowtimeDTO dto = new AddShowtimeDTO(1L, 50.0, "Theater 1",
                LocalDateTime.now(), LocalDateTime.now().plusHours(2));

        showtimeService.addShowtime(dto);

        verify(showtimeRepository, times(1)).save(any(Showtime.class));
    }

    @Test
    public void testUpdateShowtime() {
        Showtime existing = new Showtime();
        Movie movie = new Movie();
        movie.setId(1L);

        when(showtimeRepository.findById(anyLong())).thenReturn(Optional.of(existing));
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie));
        when(showtimeRepository.findOverlappingShowtimes(anyString(), any(), any())).thenReturn(Collections.emptyList());

        UpdateShowtimeDTO dto = new UpdateShowtimeDTO(1L, 60.0, "Theater 1",
                LocalDateTime.now(), LocalDateTime.now().plusHours(2));

        showtimeService.updateShowtime(1L, dto);

        verify(showtimeRepository, times(1)).save(any(Showtime.class));
    }

    @Test
    public void testDeleteShowtime() {
        when(showtimeRepository.existsById(anyLong())).thenReturn(true);

        showtimeService.deleteShowtime(1L);

        verify(showtimeRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testAddShowtimeOverlapThrowsException() {
        AddShowtimeDTO dto = new AddShowtimeDTO();
        dto.setMovieId(1L);
        dto.setPrice(30.0);
        dto.setTheater("Main Hall");
        dto.setStartTime(LocalDateTime.of(2025, 3, 23, 15, 0));
        dto.setEndTime(LocalDateTime.of(2025, 3, 23, 17, 0));

        Movie movie = new Movie();
        movie.setId(1L);

        Showtime overlapping = new Showtime();
        overlapping.setStartTime(LocalDateTime.of(2025, 3, 23, 16, 0));
        overlapping.setEndTime(LocalDateTime.of(2025, 3, 23, 18, 0));

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(showtimeRepository.findOverlappingShowtimes(
                eq("Main Hall"),
                eq(dto.getStartTime()),
                eq(dto.getEndTime()))
        ).thenReturn(List.of(overlapping));

        assertThrows(BadRequestException.class, () -> showtimeService.addShowtime(dto));
    }

}
