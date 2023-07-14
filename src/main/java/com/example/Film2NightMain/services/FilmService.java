package com.example.Film2NightMain.services;

import com.example.Film2NightMain.entities.Film;

import java.util.List;

public interface FilmService {
    Film findFilmById(int id);
    List<Film> getAllFilms();
}
