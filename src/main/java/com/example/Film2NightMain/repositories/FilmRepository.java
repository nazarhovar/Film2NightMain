package com.example.Film2NightMain.repositories;

import com.example.Film2NightMain.entities.Film;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmRepository extends JpaRepository<Film, Long> {
}
