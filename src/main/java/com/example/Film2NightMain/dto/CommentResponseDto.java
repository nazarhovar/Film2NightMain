package com.example.Film2NightMain.dto;

import java.time.LocalDateTime;

public class CommentResponseDto {
    private String author;
    private LocalDateTime createdAt;
    private String text;

    public CommentResponseDto(String author, LocalDateTime createdAt, String text) {
        this.author = author;
        this.createdAt = createdAt;
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getText() {
        return text;
    }
}
