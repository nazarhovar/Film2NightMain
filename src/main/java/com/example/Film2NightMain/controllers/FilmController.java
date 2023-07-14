package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.entities.Film;
import com.example.Film2NightMain.services.impl.FilmServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import lombok.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
public class FilmController {
    private final FilmServiceImpl filmServiceImpl;

    @GetMapping("/film/{id}")
    public Film getFilmById(@PathVariable int id) {
        Film film = filmServiceImpl.findFilmById(id);
        if (film != null) {
            System.out.println("Film found: " + film.getName_origin());
        } else {
            System.out.println("Film not found.");
        }
        return film;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        List<Film> films = filmServiceImpl.getAllFilms();
        if (!films.isEmpty()) {
            System.out.println("Films found: ");
            for (Film film : films) {
                System.out.println(film.getName_origin());
            }
        } else {
            System.out.println("No films found.");
        }
        return films;
    }
}
