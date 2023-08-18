package com.example.Film2NightMain.services.impl;

import com.example.Film2NightMain.dto.RatingDto;
import com.example.Film2NightMain.entities.Rating;
import com.example.Film2NightMain.entities.Session;
import com.example.Film2NightMain.entities.User;
import com.example.Film2NightMain.repositories.RatingRepository;
import com.example.Film2NightMain.services.RatingService;
import com.example.Film2NightMain.services.SessionService;
import com.example.Film2NightMain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final SessionService sessionService;
    private final UserService userService;

    public Session rateSession(RatingDto ratingDto) {
        Session session = sessionService.findSessionById(ratingDto.getSessionId());
        User user = userService.getUserIdFromSecurityContext();

        double rating = ratingDto.getRating();
        if (rating >= 0 && rating <= 10) {
            List<Rating> ratings = session.getRates();
            double currentAverageRating = getSessionRating(session.getId());

            double newAverageRating = ((currentAverageRating * ratings.size()) + rating) / (ratings.size() + 1);

            Rating newRating = new Rating();
            newRating.setSession(session);
            newRating.setUser(user);
            newRating.setRate(rating);
            ratings.add(newRating);

            session.setAverageRating(newAverageRating);
            session.setNumberOfRatings(ratings.size());
            ratingRepository.save(newRating);

            return sessionService.saveSession(session);
        } else {
            throw new IllegalArgumentException("Rating value must be between 0 and 10");
        }
    }

    @Override
    public double getSessionRating(Long sessionId) {
        Session session = sessionService.findSessionById(sessionId);
        List<Rating> ratings = session.getRates();
        if (ratings.isEmpty())
            return 0.0;

        double sum = 0.0;
        for (Rating rating : ratings) {
            sum += rating.getRate();
        }

        return sum / ratings.size();
    }
}
