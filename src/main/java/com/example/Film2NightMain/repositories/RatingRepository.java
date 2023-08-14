package com.example.Film2NightMain.repositories;

import com.example.Film2NightMain.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findAllBySessionId(Long id);
}
