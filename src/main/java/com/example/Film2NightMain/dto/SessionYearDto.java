package com.example.Film2NightMain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Month;
import java.util.Map;

@Data
@AllArgsConstructor
public class SessionYearDto {
    private Map<Month, Integer> mapSessions;
    private long countOfSessions;
}
