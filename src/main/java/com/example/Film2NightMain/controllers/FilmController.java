package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.dto.EntityMapper;
import com.example.Film2NightMain.dto.FilmDto;
import com.example.Film2NightMain.services.FilmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Api(tags = "Films")
public class FilmController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FilmController.class);
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @ApiOperation(value = "View a movie by id")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @GetMapping("/film/{id}")
    public ResponseEntity<FilmDto> getFilmById(@PathVariable Long id) {
        return ResponseEntity.ok(EntityMapper.toFilmDto(filmService.findFilmById(id)));
    }

    @ApiOperation(value = "View all films with optional search")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @GetMapping("/films")
    public ResponseEntity<Page<FilmDto>> getAllFilms(
            Pageable pageable,
            @RequestParam(name = "search", required = false) String search) {
        Page<FilmDto> films = (search != null && !search.isEmpty()
                ? filmService.searchFilms(search, pageable)
                : filmService.getAllFilms(pageable))
                .map(EntityMapper::toFilmDto);
        log.info("Films found: {}", films.getTotalElements());
        return ResponseEntity.ok(films);
    }

    @ApiOperation(value = "View Top250 films")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @GetMapping("/films/top250")
    public ResponseEntity<Page<FilmDto>> getTop250Films(Pageable pageable) {
        Page<FilmDto> films = filmService.getTop250Films(pageable)
                .map(EntityMapper::toFilmDto);
        return ResponseEntity.ok(films);
    }
}
