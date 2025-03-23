package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.BookingDTO.AddBookingDTO;
import com.att.tdp.popcorn_palace.dto.BookingDTO.BookingResponseDTO;
import com.att.tdp.popcorn_palace.service.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private BookingService bookingService;

    @Test
    public void testBookTicket() {
        // Given
        UUID userId = UUID.randomUUID();
        Long showtimeId = 1L;
        int seatNumber = 15;
        UUID bookingId = UUID.randomUUID();

        AddBookingDTO request = new AddBookingDTO();
        request.setUserId(userId);
        request.setSeatNumber(seatNumber);
        request.setShowtimeId(showtimeId);

        BookingResponseDTO expectedResponse = new BookingResponseDTO(bookingId);

        when(bookingService.bookTicket(request)).thenReturn(expectedResponse);

        // When
        ResponseEntity<BookingResponseDTO> response = bookingController.bookTicket(request);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(bookingId, response.getBody().getBookingId());
        verify(bookingService, times(1)).bookTicket(request);
    }
}
