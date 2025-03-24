package com.att.tdp.popcorn_palace.dto.ShowtimeDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetShowtimeDTO {

    private Long id;
    private Long movieId;
    private double price;
    private String theater;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
