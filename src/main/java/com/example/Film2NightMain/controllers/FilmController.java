package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.entities.Film;
import com.example.Film2NightMain.services.impl.FilmServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
@Api(tags = "Фильмы")
public class FilmController {
    private final FilmServiceImpl filmServiceImpl;

    @ApiOperation(value = "Посмотреть фильм по id")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/film/{id}")
    public ResponseEntity<Optional<Film>> getFilmById(@PathVariable int id) {
        return ResponseEntity.ok().body(filmServiceImpl.findFilmById(id));
    }

    @ApiOperation(value = "Посмотреть все фильмы")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/films")
    public ResponseEntity<Page<Film>> getAllFilms(
            Pageable pageable,
            @RequestParam(name = "size") int pageSize,
            @RequestParam(name = "page") int pageNumber) {
        try {
            Page<Film> films = filmServiceImpl.getAllFilms(pageable);
            if (!films.isEmpty()) {
                log.info("Films found: {}", films.getSize());
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

