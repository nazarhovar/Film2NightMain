package com.example.Film2NightMain.repositories;

import com.example.Film2NightMain.entities.Rating;
import com.example.Film2NightMain.entities.Session;
import com.example.Film2NightMain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Rating findBySessionAndUser(Session session, User user);
    List<Rating> findBySession(Session session);
}
