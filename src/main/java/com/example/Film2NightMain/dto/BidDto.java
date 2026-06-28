package com.example.Film2NightMain.dto;

import com.example.Film2NightMain.entities.BidType;

public class BidDto {
    private long sessionId;
    private BidType bidType;

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public BidType getBidType() {
        return bidType;
    }

    public void setBidType(BidType bidType) {
        this.bidType = bidType;
    }
}
