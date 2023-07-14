package com.example.Film2NightMain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class SessionDto {
    private int filmId;
    private int maxVisitorCount;
    private LocalDateTime startTime;
    private Long creatorId;
    private List<Long> users;
}