package com.example.Film2NightMain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BidDeleteUserDto {
    private long bidId;
    private String username;
    private Boolean isBlocked;
    private String statusType;
    private String bidType;
}
