package com.example.Film2NightMain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RatingDto {
    private Long sessionId;
    private double rating;
}
