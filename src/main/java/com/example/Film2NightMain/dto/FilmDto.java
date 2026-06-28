package com.example.Film2NightMain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class FilmDto {
    @JsonProperty("kinopoisk_id")
    private Long kinopoiskId;
    @JsonProperty("name_origin")
    private String nameOrigin;
    @JsonProperty("poster_url")
    private String posterUrl;
    @JsonProperty("rating_kinopoisk")
    private Float ratingKinopoisk;
    @JsonProperty("rating_kinopoisk_vote_count")
    private Integer ratingKinopoiskVoteCount;
    @JsonProperty("web_url")
    private String webUrl;
    @JsonProperty("year")
    private int filmYear;
    @JsonProperty("film_length")
    private int filmLength;
    @JsonProperty("last_sync")
    private LocalDateTime lastSync;
    @JsonProperty("is_blocked")
    private Boolean isBlocked;
    @JsonProperty("trailer_url")
    private String trailerUrl;

    public FilmDto(Long kinopoiskId, String nameOrigin, String posterUrl, Float ratingKinopoisk,
                   Integer ratingKinopoiskVoteCount, String webUrl, int filmYear, int filmLength,
                   LocalDateTime lastSync, Boolean isBlocked, String trailerUrl) {
        this.kinopoiskId = kinopoiskId;
        this.nameOrigin = nameOrigin;
        this.posterUrl = posterUrl;
        this.ratingKinopoisk = ratingKinopoisk;
        this.ratingKinopoiskVoteCount = ratingKinopoiskVoteCount;
        this.webUrl = webUrl;
        this.filmYear = filmYear;
        this.filmLength = filmLength;
        this.lastSync = lastSync;
        this.isBlocked = isBlocked;
        this.trailerUrl = trailerUrl;
    }

    public Long getKinopoisk_id() { return kinopoiskId; }
    public String getName_origin() { return nameOrigin; }
    public String getPoster_url() { return posterUrl; }
    public Float getRating_kinopoisk() { return ratingKinopoisk; }
    public Integer getRating_kinopoisk_vote_count() { return ratingKinopoiskVoteCount; }
    public String getWeb_url() { return webUrl; }
    public int getYear() { return filmYear; }
    public int getFilm_length() { return filmLength; }
    public LocalDateTime getLast_sync() { return lastSync; }
    public Boolean getIs_blocked() { return isBlocked; }
    public String getTrailer_url() { return trailerUrl; }
}
