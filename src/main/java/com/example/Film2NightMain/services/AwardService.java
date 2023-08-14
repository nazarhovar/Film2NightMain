package com.example.Film2NightMain.services;

import com.example.Film2NightMain.dto.AwardDto;
import com.example.Film2NightMain.dto.AwardSessionDto;

public interface AwardService {
    AwardSessionDto setAwardToSession(AwardDto awardDto);
}
