package com.example.Film2NightMain.dto;

public class RatingDto {
    private Long sessionId;
    private double rating;

    public RatingDto() {
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
