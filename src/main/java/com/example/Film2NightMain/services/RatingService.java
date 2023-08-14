package com.example.Film2NightMain.services;

import com.example.Film2NightMain.dto.RatingDto;
import com.example.Film2NightMain.entities.Session;


public interface RatingService {
    Session rateSession(RatingDto ratingDto);
    double getSessionRating(Long sessionId);
}
