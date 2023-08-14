package com.example.Film2NightMain.dto;

import com.example.Film2NightMain.entities.BidType;
import com.example.Film2NightMain.entities.Film;
import com.example.Film2NightMain.entities.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BidInfoDto {
    private Long bidId;
    private long sessionId;
    private String username;
    private Film filmId;
    private boolean isBlocked;
    private Status statusType;
    private BidType bidType;
}
