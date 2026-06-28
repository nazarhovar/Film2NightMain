package com.example.Film2NightMain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class SessionResponseDto {
    private Long id;
    private LocalDateTime startTime;
    private Boolean isCanceled;
    private int visitorCount;
    private int maxVisitorCount;
    @JsonProperty("filmId")
    private FilmDto film;
    private UserResponseDto creator;
    private Integer numberOfRatings;
    private Double averageRating;

    public SessionResponseDto(Long id, LocalDateTime startTime, Boolean isCanceled, int visitorCount,
                              int maxVisitorCount, FilmDto film, UserResponseDto creator,
                              Integer numberOfRatings, Double averageRating) {
        this.id = id;
        this.startTime = startTime;
        this.isCanceled = isCanceled;
        this.visitorCount = visitorCount;
        this.maxVisitorCount = maxVisitorCount;
        this.film = film;
        this.creator = creator;
        this.numberOfRatings = numberOfRatings;
        this.averageRating = averageRating;
    }

    public Long getId() { return id; }
    public LocalDateTime getStartTime() { return startTime; }
    public Boolean getIsCanceled() { return isCanceled; }
    public int getVisitorCount() { return visitorCount; }
    public int getMaxVisitorCount() { return maxVisitorCount; }
    public FilmDto getFilmId() { return film; }
    public UserResponseDto getCreator() { return creator; }
    public Integer getNumberOfRatings() { return numberOfRatings; }
    public Double getAverageRating() { return averageRating; }
}
