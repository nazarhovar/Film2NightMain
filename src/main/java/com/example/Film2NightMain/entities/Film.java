package com.example.Film2NightMain.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Film {
    @Id
    private Long kinopoisk_id;
    @Column
    private String name_origin;
    @Column
    private String poster_url;
    @Column
    private Float rating_kinopoisk;
    @Column
    private Integer rating_kinopoisk_vote_count;
    @Column
    private String web_url;
    @JsonProperty("year")
    @Column(name = "film_year")
    private int filmYear;
    @Column
    private int film_length;
    @Column
    private LocalDateTime last_sync;
    @Column
    private Boolean is_blocked;
    @Column
    private String trailer_url;
    @ManyToMany
    private Set<Block> blockSet = new HashSet<>();

    public Film() {
    }

    public Long getKinopoisk_id() {
        return kinopoisk_id;
    }

    public String getName_origin() {
        return name_origin;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public Float getRating_kinopoisk() {
        return rating_kinopoisk;
    }

    public Integer getRating_kinopoisk_vote_count() {
        return rating_kinopoisk_vote_count;
    }

    public String getWeb_url() {
        return web_url;
    }

    public int getFilmYear() {
        return filmYear;
    }

    public int getFilm_length() {
        return film_length;
    }

    public LocalDateTime getLast_sync() {
        return last_sync;
    }

    public Boolean getIs_blocked() {
        return is_blocked;
    }

    public void setIs_blocked(Boolean is_blocked) {
        this.is_blocked = is_blocked;
    }

    public String getTrailer_url() {
        return trailer_url;
    }

    public Set<Block> getBlockSet() {
        return blockSet;
    }

}
