package com.example.Film2NightMain.services.impl;

import com.example.Film2NightMain.entities.Film;
import com.example.Film2NightMain.repositories.FilmRepository;
import com.example.Film2NightMain.services.FilmService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;

    public FilmServiceImpl(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public Film findFilmById(long id) {
        return filmRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Film not found"));
    }

    public Page<Film> getAllFilms(Pageable pageable) {
        return filmRepository.findAll(pageable);
    }

    public Page<Film> searchFilms(String search, Pageable pageable) {
        return filmRepository.searchByName(search, pageable);
    }

    public Page<Film> getTop250Films(Pageable pageable) {
        return filmRepository.findTop250(pageable);
    }

    @Override
    public void saveFilm(Film film) {
        filmRepository.save(film);
    }
}
