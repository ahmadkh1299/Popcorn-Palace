package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.ShowtimeDTO.AddShowtimeDTO;
import com.att.tdp.popcorn_palace.dto.ShowtimeDTO.GetShowtimeDTO;
import com.att.tdp.popcorn_palace.dto.ShowtimeDTO.UpdateShowtimeDTO;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShowtimeControllerTest {

    @InjectMocks
    private ShowtimeController showtimeController;

    @Mock
    private ShowtimeService showtimeService;

    @Test
    public void testGetShowtimeById() {
        GetShowtimeDTO dto = new GetShowtimeDTO();
        dto.setId(1L);

        when(showtimeService.getShowtimeById(1L)).thenReturn(dto);

        ResponseEntity<GetShowtimeDTO> response = showtimeController.getShowtime(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(showtimeService, times(1)).getShowtimeById(1L);
    }

    @Test
    public void testGetAllShowtimes() {
        GetShowtimeDTO dto = new GetShowtimeDTO();
        dto.setId(1L);

        when(showtimeService.getAllShowtimes()).thenReturn(Collections.singletonList(dto));

        ResponseEntity<List<GetShowtimeDTO>> response = showtimeController.getAllShowtimes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(showtimeService, times(1)).getAllShowtimes();
    }

    @Test
    public void testAddShowtime() {
        AddShowtimeDTO addDto = new AddShowtimeDTO();
        addDto.setTheater("IMAX");
        addDto.setStartTime(LocalDateTime.now());
        addDto.setEndTime(LocalDateTime.now().plusHours(2));

        doNothing().when(showtimeService).addShowtime(addDto);

        ResponseEntity<Void> response = showtimeController.addShowtime(addDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(showtimeService, times(1)).addShowtime(addDto);
    }

    @Test
    public void testUpdateShowtime() {
        UpdateShowtimeDTO updateDto = new UpdateShowtimeDTO();

        doNothing().when(showtimeService).updateShowtime(1L, updateDto);

        ResponseEntity<Void> response = showtimeController.updateShowtime(1L, updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(showtimeService, times(1)).updateShowtime(1L, updateDto);
    }

    @Test
    public void testDeleteShowtime() {
        doNothing().when(showtimeService).deleteShowtime(1L);

        ResponseEntity<Void> response = showtimeController.deleteShowtime(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(showtimeService, times(1)).deleteShowtime(1L);
    }
}
