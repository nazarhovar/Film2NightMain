package com.example.Film2NightMain.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Film {
    @Id
    private Integer kinopoisk_id;
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
    @Column
    private int year;
    @Column
    private int film_length;
    @Column
    private String last_sync;
    @Column
    private Boolean is_blocked;
}