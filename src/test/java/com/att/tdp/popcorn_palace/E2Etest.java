
package com.att.tdp.popcorn_palace;

import com.att.tdp.popcorn_palace.dto.BookingDTO.AddBookingDTO;
import com.att.tdp.popcorn_palace.dto.MovieDTO.AddMovieDTO;
import com.att.tdp.popcorn_palace.dto.ShowtimeDTO.AddShowtimeDTO;
import com.att.tdp.popcorn_palace.dto.ShowtimeDTO.UpdateShowtimeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    public void testInvalidMovieAndBookingScenarios() {
        // Try creating a showtime with a non-existent movie ID
        AddShowtimeDTO invalidShowtime = new AddShowtimeDTO(9999L, 30.0, "Hall Z",
                LocalDateTime.of(2025, 4, 2, 12, 0),
                LocalDateTime.of(2025, 4, 2, 14, 0));
        ResponseEntity<String> invalidShowtimeResponse = restTemplate.postForEntity("/showtimes", invalidShowtime, String.class);
        assertEquals(HttpStatus.NOT_FOUND, invalidShowtimeResponse.getStatusCode());

        // First create a valid showtime for Movie A
        AddShowtimeDTO validShowtime = new AddShowtimeDTO(1L, 20.0, "Hall Y",
                LocalDateTime.of(2025, 4, 2, 15, 0),
                LocalDateTime.of(2025, 4, 2, 17, 0));
        ResponseEntity<Void> validShowtimeResponse = restTemplate.postForEntity("/showtimes", validShowtime, Void.class);
        assertEquals(HttpStatus.CREATED, validShowtimeResponse.getStatusCode());

        // Try booking with invalid showtime ID
        AddBookingDTO invalidBooking = new AddBookingDTO(UUID.randomUUID(), 5, 9999L);
        ResponseEntity<String> invalidBookingResponse = restTemplate.postForEntity("/bookings", invalidBooking, String.class);
        assertEquals(HttpStatus.NOT_FOUND, invalidBookingResponse.getStatusCode());

        // Try booking with invalid seat number (zero or negative)
        AddBookingDTO zeroSeatBooking = new AddBookingDTO(UUID.randomUUID(), 0, 1L);
        ResponseEntity<String> zeroSeatBookingResponse = restTemplate.postForEntity("/bookings", zeroSeatBooking, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, zeroSeatBookingResponse.getStatusCode());

        AddBookingDTO negativeSeatBooking = new AddBookingDTO(UUID.randomUUID(), -1, 1L);
        ResponseEntity<String> negativeSeatBookingResponse = restTemplate.postForEntity("/bookings", negativeSeatBooking, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, negativeSeatBookingResponse.getStatusCode());
    }

    @Test
    public void testUpdateAndDeleteShowtime() {
        // Create first showtime (Movie A)
        AddShowtimeDTO showtime1 = new AddShowtimeDTO(1L, 20.0, "Hall X",
                LocalDateTime.of(2025, 4, 1, 14, 0),
                LocalDateTime.of(2025, 4, 1, 16, 0));
        ResponseEntity<Void> showtime1Response = restTemplate.postForEntity("/showtimes", showtime1, Void.class);
        assertEquals(HttpStatus.CREATED, showtime1Response.getStatusCode());

        // Create second showtime (Movie B, no overlap)
        AddShowtimeDTO showtime2 = new AddShowtimeDTO(2L, 22.0, "Hall X",
                LocalDateTime.of(2025, 4, 1, 16, 30),
                LocalDateTime.of(2025, 4, 1, 18, 30));
        ResponseEntity<Void> showtime2Response = restTemplate.postForEntity("/showtimes", showtime2, Void.class);
        assertEquals(HttpStatus.CREATED, showtime2Response.getStatusCode());

        // Get all showtimes to retrieve second showtime's ID
        ResponseEntity<Object[]> allShowtimes = restTemplate.getForEntity("/showtimes", Object[].class);
        assertEquals(HttpStatus.OK, allShowtimes.getStatusCode());
        assertNotNull(allShowtimes.getBody());
        assertTrue(allShowtimes.getBody().length >= 2);

        // Assuming the second created showtime is last
        Long showtime2Id = Long.valueOf(((LinkedHashMap<?, ?>) allShowtimes.getBody()[1]).get("id").toString());

        // Try updating showtime2 to overlap with showtime1
        var overlapUpdate = new com.att.tdp.popcorn_palace.dto.ShowtimeDTO.UpdateShowtimeDTO(
                2L, 23.0, "Hall X",
                LocalDateTime.of(2025, 4, 1, 15, 30),
                LocalDateTime.of(2025, 4, 1, 17, 0));
        HttpEntity<Object> overlapRequest = new HttpEntity<>(overlapUpdate);
        ResponseEntity<String> overlapResponse = restTemplate.exchange("/showtimes/" + showtime2Id, HttpMethod.PUT, overlapRequest, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, overlapResponse.getStatusCode());

        // Now update showtime2 to a valid time slot
        var validUpdate = new com.att.tdp.popcorn_palace.dto.ShowtimeDTO.UpdateShowtimeDTO(
                2L, 23.0, "Hall X",
                LocalDateTime.of(2025, 4, 1, 18, 45),
                LocalDateTime.of(2025, 4, 1, 20, 45));
        HttpEntity<Object> validRequest = new HttpEntity<>(validUpdate);
        ResponseEntity<String> validUpdateResponse = restTemplate.exchange("/showtimes/" + showtime2Id, HttpMethod.PUT, validRequest, String.class);
        assertEquals(HttpStatus.OK, validUpdateResponse.getStatusCode());

        //  Finally, delete the updated showtime
        ResponseEntity<Void> deleteResponse = restTemplate.exchange("/showtimes/" + showtime2Id, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
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

    @Test
    public void testUpdateShowtimeToCauseOverlapByMovieDurationChange() {
        // 1. Create Movies A, B, C
        Long movieAId = createMovieAndGetId(new AddMovieDTO("Movie A - Morning", List.of("Action"), 120, 8.5, 2023));
        Long movieBId = createMovieAndGetId(new AddMovieDTO("Movie B - Noon", List.of("Drama"), 90, 7.0, 2022));
        Long movieCId = createMovieAndGetId(new AddMovieDTO("Movie C - Afternoon", List.of("Sci-Fi"), 100, 8.0, 2021));

        assertNotNull(movieAId);
        assertNotNull(movieBId);
        assertNotNull(movieCId);

        // 2. Create non-overlapping showtimes (different blocks)
        // A: 08:00 → 10:00
        LocalDateTime startA = LocalDateTime.of(2025, 4, 1, 8, 0);
        LocalDateTime endA = startA.plusMinutes(120);
        postShowtime(new AddShowtimeDTO(movieAId, 25.0, "Hall Z", startA, endA));

        // B: 11:00 → 12:30 (duration 90min)
        LocalDateTime startB = LocalDateTime.of(2025, 4, 1, 11, 0);
        LocalDateTime endB = startB.plusMinutes(90);
        postShowtime(new AddShowtimeDTO(movieBId, 25.0, "Hall Z", startB, endB));

        // C: 13:00 → 14:40 (duration 100min)
        LocalDateTime startC = LocalDateTime.of(2025, 4, 1, 13, 0);
        LocalDateTime endC = startC.plusMinutes(100);
        postShowtime(new AddShowtimeDTO(movieCId, 25.0, "Hall Z", startC, endC));

        // 3. Get showtime ID for Movie B
        ResponseEntity<Object[]> allShowtimes = restTemplate.getForEntity("/showtimes", Object[].class);
        assertEquals(HttpStatus.OK, allShowtimes.getStatusCode());

        Long showtimeBId = null;
        for (Object obj : allShowtimes.getBody()) {
            if (obj instanceof LinkedHashMap<?, ?> map && movieBId.equals(Long.valueOf(map.get("movieId").toString()))) {
                showtimeBId = Long.valueOf(map.get("id").toString());
                break;
            }
        }
        assertNotNull(showtimeBId, "Showtime B not found");

        // 4. Update B's showtime to simulate longer movie (duration now 150 min)
        // New end time: 11:00 → 13:30 → overlaps with Movie C
        UpdateShowtimeDTO updateToCauseOverlap = new UpdateShowtimeDTO(
                movieBId,
                25.0,
                "Hall Z",
                startB,
                startB.plusMinutes(150) // Overlaps with C
        );

        HttpEntity<UpdateShowtimeDTO> request = new HttpEntity<>(updateToCauseOverlap);
        ResponseEntity<String> overlapResponse = restTemplate.exchange(
                "/showtimes/" + showtimeBId,
                HttpMethod.PUT,
                request,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, overlapResponse.getStatusCode(), "Expected overlap error after movie duration update");
        assertNotNull(overlapResponse.getBody(), "Expected error response body");
        System.out.println("Overlap by movie duration update correctly detected: " + overlapResponse.getBody());
    }




    private Long createMovieAndGetId(AddMovieDTO dto) {
        ResponseEntity<Map> response = restTemplate.postForEntity("/movies", dto, Map.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Failed to create movie");
        assertNotNull(response.getBody(), "Movie creation response body is null");
        assertTrue(response.getBody().containsKey("id"), "Movie creation response missing 'id'");
        return ((Number) response.getBody().get("id")).longValue();
    }


    private void postShowtime(AddShowtimeDTO dto) {
        ResponseEntity<String> response = restTemplate.postForEntity("/showtimes", dto, String.class);
        if (response.getStatusCode() != HttpStatus.CREATED) {
            System.out.println("Showtime creation failed: " + response.getBody());
        }
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Failed to create showtime");
    }

}
