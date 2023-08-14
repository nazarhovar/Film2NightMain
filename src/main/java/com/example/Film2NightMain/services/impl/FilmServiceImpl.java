package com.example.Film2NightMain.services.impl;

import com.example.Film2NightMain.entities.Film;
import com.example.Film2NightMain.repositories.FilmRepository;
import com.example.Film2NightMain.services.FilmService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import lombok.*;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;

    public Film findFilmById(long id) {
        return filmRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Film not found with ID: " + id));
    }

    public Page<Film> getAllFilms(Pageable pageable) {
        return filmRepository.findAll(pageable);
    }

    @Override
    public void saveFilm(Film film) {
        filmRepository.save(film);
    }
}
