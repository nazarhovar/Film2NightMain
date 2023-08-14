package com.example.Film2NightMain.dto;

import lombok.Data;

@Data
public class RatingDto {
    private Long sessionId;
    private Long userId;
    private double rating;
}
