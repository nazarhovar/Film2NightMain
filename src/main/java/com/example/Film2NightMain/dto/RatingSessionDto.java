package com.example.Film2NightMain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RatingSessionDto {
    private Long sessionId;
    private double rating;
}
