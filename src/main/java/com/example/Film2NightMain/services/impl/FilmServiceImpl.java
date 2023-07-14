package com.example.Film2NightMain.services.impl;

import com.example.Film2NightMain.entities.Film;
import com.example.Film2NightMain.repositories.FilmRepository;
import com.example.Film2NightMain.services.FilmService;
import org.springframework.stereotype.Service;
import lombok.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;

    public Film findFilmById(int id) {
        return filmRepository.findById(id).orElse(null);
    }

    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }
}
