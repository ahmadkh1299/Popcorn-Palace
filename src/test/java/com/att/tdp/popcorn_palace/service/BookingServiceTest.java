package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.BookingDTO.AddBookingDTO;
import com.att.tdp.popcorn_palace.dto.BookingDTO.BookingResponseDTO;
import com.att.tdp.popcorn_palace.entities.Booking;
import com.att.tdp.popcorn_palace.entities.Showtime;
import com.att.tdp.popcorn_palace.exception.BadRequestException;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ShowtimeRepository showtimeRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBookTicket() {
        Showtime showtime = new Showtime();
        showtime.setId(1L);

        AddBookingDTO dto = new AddBookingDTO();
        dto.setUserId(UUID.randomUUID());
        dto.setSeatNumber(15);
        dto.setShowtimeId(1L);

        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(bookingRepository.existsByShowtimeIdAndSeatNumber(1L, 15)).thenReturn(false);

        Booking mockBooking = new Booking();
        UUID generatedId = UUID.randomUUID();
        mockBooking.setBookingId(generatedId);
        mockBooking.setUserId(dto.getUserId());
        mockBooking.setSeatNumber(dto.getSeatNumber());
        mockBooking.setShowtime(showtime);

        when(bookingRepository.save(any(Booking.class))).thenReturn(mockBooking);

        BookingResponseDTO response = bookingService.bookTicket(dto);

        assertEquals(generatedId, response.getBookingId());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    public void testDoubleBookingThrowsException() {
        AddBookingDTO dto = new AddBookingDTO();
        dto.setUserId(UUID.randomUUID());
        dto.setSeatNumber(10);
        dto.setShowtimeId(1L);

        Showtime showtime = new Showtime();
        showtime.setId(1L);

        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(bookingRepository.existsByShowtimeIdAndSeatNumber(1L, 10)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> bookingService.bookTicket(dto));
    }
}
