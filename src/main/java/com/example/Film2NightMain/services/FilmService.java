package com.example.Film2NightMain.services;

import com.example.Film2NightMain.entities.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FilmService {
    Optional<Film> findFilmById(long id);

    Page<Film> getAllFilms(Pageable pageable);

    void saveFilm(Film film);
}
