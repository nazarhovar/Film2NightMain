package com.example.Film2NightMain.dto;

import com.example.Film2NightMain.entities.Film;
import com.example.Film2NightMain.entities.Session;
import com.example.Film2NightMain.entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class EntityMapper {

    public static FilmDto toFilmDto(Film film) {
        if (film == null) return null;
        return new FilmDto(
                film.getKinopoisk_id(), film.getName_origin(), film.getPoster_url(),
                film.getRating_kinopoisk(), film.getRating_kinopoisk_vote_count(),
                film.getWeb_url(), film.getFilmYear(), film.getFilm_length(),
                film.getLast_sync(), film.getIs_blocked(), film.getTrailer_url()
        );
    }

    public static UserResponseDto toUserDto(User user) {
        if (user == null) return null;
        List<String> roles = user.getRoles() != null
                ? user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList())
                : List.of();
        return new UserResponseDto(
                user.getId(), user.getUsername(), user.getFirstName(),
                user.getLastName(), user.getIsBlocked(), roles
        );
    }

    public static SessionResponseDto toSessionDto(Session session) {
        if (session == null) return null;
        return new SessionResponseDto(
                session.getId(), session.getStartTime(), session.getIsCanceled(),
                session.getVisitorCount(), session.getMaxVisitorCount(),
                toFilmDto(session.getFilmId()), toUserDto(session.getCreator()),
                session.getNumberOfRatings(), session.getAverageRating()
        );
    }
}
