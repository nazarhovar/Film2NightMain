package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.entities.Film;
import com.example.Film2NightMain.services.impl.FilmServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Film> getFilmById(@PathVariable int id) {
        try {
            Film film = filmServiceImpl.findFilmById(id);
            if (film != null) {
                logger.info("Film found: {}", film.getName_origin());
                return ResponseEntity.ok(film);
            } else {
                logger.info("Film not found.");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error getting film: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/films")
    public ResponseEntity<List<Film>> getAllFilms() {
        try {
            List<Film> films = filmServiceImpl.getAllFilms();
            if (!films.isEmpty()) {
                logger.info("Films found: {}", films.size());
                return ResponseEntity.ok(films);
            } else {
                logger.info("No films found.");
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            logger.error("Error getting list of films: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
