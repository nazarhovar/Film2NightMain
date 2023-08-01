package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.entities.Film;
import com.example.Film2NightMain.services.impl.FilmServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class FilmController {
    private final FilmServiceImpl filmServiceImpl;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/film/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable int id) {
        try {
            Film film = filmServiceImpl.findFilmById(id);
            if (film != null) {
                log.info("Film found: {}", film.getName_origin());
                return ResponseEntity.ok(film);
            } else {
                log.info("Film not found.");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error getting film: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/films")
    public ResponseEntity<List<Film>> getAllFilms() {
        try {
            List<Film> films = filmServiceImpl.getAllFilms();
            if (!films.isEmpty()) {
                log.info("Films found: {}", films.size());
                return ResponseEntity.ok(films);
            } else {
                log.info("No films found.");
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            log.error("Error getting list of films: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
