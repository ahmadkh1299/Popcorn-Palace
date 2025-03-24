
package com.att.tdp.popcorn_palace;

import com.att.tdp.popcorn_palace.dto.BookingDTO.AddBookingDTO;
import com.att.tdp.popcorn_palace.dto.MovieDTO.AddMovieDTO;
import com.att.tdp.popcorn_palace.dto.ShowtimeDTO.AddShowtimeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class E2Etest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Long movie1Id;
    private Long movie2Id;
    private Long showtime1Id;
    private Long showtime2Id;

    @BeforeEach
    public void setup() {
        // Create movie 1
        AddMovieDTO movie1 = new AddMovieDTO("Movie A", List.of("Action"), 120, 8.5, 2020);
        ResponseEntity<Void> movie1Response = restTemplate.postForEntity("/movies", movie1, Void.class);
        assertEquals(HttpStatus.CREATED, movie1Response.getStatusCode());

        // Create movie 2
        AddMovieDTO movie2 = new AddMovieDTO("Movie B", List.of("Drama"), 100, 7.8, 2022);
        ResponseEntity<Void> movie2Response = restTemplate.postForEntity("/movies", movie2, Void.class);
        assertEquals(HttpStatus.CREATED, movie2Response.getStatusCode());
    }

    @Test
    public void testShowtimeAndBookingFlow() {
        // Create valid showtime
        AddShowtimeDTO showtime1 = new AddShowtimeDTO(1L, 25.0, "Hall 1",
                LocalDateTime.of(2025, 3, 30, 10, 0),
                LocalDateTime.of(2025, 3, 30, 12, 0));
        ResponseEntity<Void> showtime1Response = restTemplate.postForEntity("/showtimes", showtime1, Void.class);
        assertEquals(HttpStatus.CREATED, showtime1Response.getStatusCode());

        // Try to create overlapping showtime
        AddShowtimeDTO showtimeOverlap = new AddShowtimeDTO(1L, 30.0, "Hall 1",
                LocalDateTime.of(2025, 3, 30, 11, 0),
                LocalDateTime.of(2025, 3, 30, 13, 0));
        ResponseEntity<String> showtimeOverlapResponse = restTemplate.postForEntity("/showtimes", showtimeOverlap, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, showtimeOverlapResponse.getStatusCode());

        // Book a ticket
        UUID userId = UUID.randomUUID();
        AddBookingDTO booking = new AddBookingDTO(userId, 10, 1L);
        ResponseEntity<String> bookingResponse = restTemplate.postForEntity("/bookings", booking, String.class);
        assertEquals(HttpStatus.CREATED, bookingResponse.getStatusCode());

        // Try to double-book the same seat
        AddBookingDTO duplicateBooking = new AddBookingDTO(userId, 10, 1L);
        ResponseEntity<String> duplicateResponse = restTemplate.postForEntity("/bookings", duplicateBooking, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, duplicateResponse.getStatusCode());
    }
}
