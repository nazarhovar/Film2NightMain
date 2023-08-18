package com.example.Film2NightMain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BidDeleteFilmDto {
    private long filmId;
    private long blockId;
}
