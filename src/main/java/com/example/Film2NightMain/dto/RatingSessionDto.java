package com.example.Film2NightMain.dto;

public class RatingSessionDto {
    private Long sessionId;
    private double rating;

    public RatingSessionDto() {
    }

    public RatingSessionDto(Long sessionId, double rating) {
        this.sessionId = sessionId;
        this.rating = rating;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public double getRating() {
        return rating;
    }

}
