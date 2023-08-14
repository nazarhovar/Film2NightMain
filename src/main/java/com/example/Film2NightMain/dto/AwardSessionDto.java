package com.example.Film2NightMain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AwardSessionDto {
    private Long sessionId;
    private LocalDateTime sessionDate;
    private Long awardId;
    private String awardName;
}
