package com.example.Film2NightMain.dto;

import com.example.Film2NightMain.entities.BidType;
import lombok.Data;

@Data
public class BidDto {
    private long sessionId;
    private BidType bidType;
}
