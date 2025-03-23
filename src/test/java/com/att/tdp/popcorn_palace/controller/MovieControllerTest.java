package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.MovieDTO.AddMovieDTO;
import com.att.tdp.popcorn_palace.dto.MovieDTO.GetMovieDTO;
import com.att.tdp.popcorn_palace.dto.MovieDTO.UpdateMovieDTO;
import com.att.tdp.popcorn_palace.service.MovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieControllerTest {

    @InjectMocks
    private MovieController movieController;

    @Mock
    private MovieService movieService;

    @Test
    public void testGetAllMovies() {
        GetMovieDTO dto = new GetMovieDTO();
        dto.setTitle("Sample Movie");

        when(movieService.getAllMovies()).thenReturn(Collections.singletonList(dto));

        ResponseEntity<List<GetMovieDTO>> response = movieController.getAllMovies();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(movieService, times(1)).getAllMovies();
    }

    @Test
    public void testGetMovieByTitle() {
        GetMovieDTO dto = new GetMovieDTO();
        dto.setTitle("Inception");

        when(movieService.getMovieByTitle("Inception")).thenReturn(dto);

        ResponseEntity<GetMovieDTO> response = movieController.getMovieByTitle("Inception");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Inception", response.getBody().getTitle());
        verify(movieService, times(1)).getMovieByTitle("Inception");
    }

    @Test
    public void testAddMovie() {
        AddMovieDTO addDto = new AddMovieDTO();

        doNothing().when(movieService).addMovie(addDto);

        ResponseEntity<Void> response = movieController.addMovie(addDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(movieService, times(1)).addMovie(addDto);
    }

    @Test
    public void testUpdateMovie() {
        UpdateMovieDTO updateDto = new UpdateMovieDTO();

        doNothing().when(movieService).updateMovie("Old Title", updateDto);

        ResponseEntity<Void> response = movieController.updateMovie("Old Title", updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(movieService, times(1)).updateMovie("Old Title", updateDto);
    }

    @Test
    public void testDeleteMovie() {
        doNothing().when(movieService).deleteMovie("Avatar");

        ResponseEntity<Void> response = movieController.deleteMovie("Avatar");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(movieService, times(1)).deleteMovie("Avatar");
    }
}
