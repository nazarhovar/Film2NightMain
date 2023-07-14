package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.entities.Film;
import com.example.Film2NightMain.services.impl.FilmServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import lombok.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FilmController {
    private final FilmServiceImpl filmServiceImpl;
    private static final Logger logger = LoggerFactory.getLogger(FilmController.class);

    @GetMapping("/film/{id}")
    public Film getFilmById(@PathVariable int id) {
        Film film = filmServiceImpl.findFilmById(id);
        if (film != null) {
            logger.info("Film found: {}", film.getName_origin());
        } else {
            logger.info("Film not found.");
        }

        return film;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        List<Film> films = filmServiceImpl.getAllFilms();
        if (!films.isEmpty()) {
            logger.info("Films found: ");
            for (Film film : films) {
                logger.info(film.getName_origin());
            }
        } else {
            logger.info("No films found.");
        }

        return films;
    }
}
