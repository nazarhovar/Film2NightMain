package com.example.Film2NightMain.services.impl;

import com.example.Film2NightMain.dto.RatingDto;
import com.example.Film2NightMain.entities.Rating;
import com.example.Film2NightMain.entities.Session;
import com.example.Film2NightMain.entities.User;
import com.example.Film2NightMain.repositories.RatingRepository;
import com.example.Film2NightMain.services.RatingService;
import com.example.Film2NightMain.services.SessionService;
import com.example.Film2NightMain.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final SessionService sessionService;
    private final UserService userService;

    public RatingServiceImpl(RatingRepository ratingRepository, SessionService sessionService, UserService userService) {
        this.ratingRepository = ratingRepository;
        this.sessionService = sessionService;
        this.userService = userService;
    }

    public Session rateSession(RatingDto ratingDto) {
        Session session = sessionService.findSessionById(ratingDto.getSessionId());
        User user = userService.getUserIdFromSecurityContext();

        double rating = ratingDto.getRating();
        if (rating < 0 || rating > 10) {
            throw new IllegalArgumentException("Rating value must be between 0 and 10");
        }

        Rating existingRating = ratingRepository.findBySessionAndUser(session, user);
        if (existingRating != null) {
            existingRating.setRate(rating);
            ratingRepository.save(existingRating);
            List<Rating> allRatings = ratingRepository.findBySession(session);
            double sum = allRatings.stream().mapToDouble(Rating::getRate).sum();
            session.setAverageRating(sum / allRatings.size());
            session.setNumberOfRatings(allRatings.size());
            return sessionService.saveSession(session);
        }

        Rating newRating = new Rating();
        newRating.setSession(session);
        newRating.setUser(user);
        newRating.setRate(rating);
        ratingRepository.save(newRating);

        List<Rating> allRatings = ratingRepository.findBySession(session);
        double sum = allRatings.stream().mapToDouble(Rating::getRate).sum();
        session.setAverageRating(sum / allRatings.size());
        session.setNumberOfRatings(allRatings.size());

        return sessionService.saveSession(session);
    }

}
