package com.example.Film2NightMain.dto;

import com.example.Film2NightMain.entities.BidType;
import com.example.Film2NightMain.entities.Film;
import com.example.Film2NightMain.entities.Status;

public class BidInfoDto {
    private Long bidId;
    private long sessionId;
    private String username;
    private Film filmId;
    private boolean isBlocked;
    private Status statusType;
    private BidType bidType;

    public BidInfoDto(Long bidId, long sessionId, String username, Film filmId, boolean isBlocked, Status statusType, BidType bidType) {
        this.bidId = bidId;
        this.sessionId = sessionId;
        this.username = username;
        this.filmId = filmId;
        this.isBlocked = isBlocked;
        this.statusType = statusType;
        this.bidType = bidType;
    }

    public Long getBidId() {
        return bidId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public String getUsername() {
        return username;
    }

    public Film getFilmId() {
        return filmId;
    }

    public boolean isIsBlocked() {
        return isBlocked;
    }

    public Status getStatusType() {
        return statusType;
    }

    public BidType getBidType() {
        return bidType;
    }

}
