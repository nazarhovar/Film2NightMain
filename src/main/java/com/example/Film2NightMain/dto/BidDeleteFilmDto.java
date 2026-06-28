package com.example.Film2NightMain.dto;

public class BidDeleteFilmDto {
    private long filmId;
    private long blockId;

    public BidDeleteFilmDto() {
    }

    public long getFilmId() {
        return filmId;
    }

    public void setFilmId(long filmId) {
        this.filmId = filmId;
    }

    public long getBlockId() {
        return blockId;
    }

    public void setBlockId(long blockId) {
        this.blockId = blockId;
    }
}
