package com.example.Film2NightMain.dto;

import java.time.LocalDateTime;

public class SessionDto {
    private int filmId;
    private int maxVisitorCount;
    private LocalDateTime startTime;

    public SessionDto() {
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public int getMaxVisitorCount() {
        return maxVisitorCount;
    }

    public void setMaxVisitorCount(int maxVisitorCount) {
        this.maxVisitorCount = maxVisitorCount;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
