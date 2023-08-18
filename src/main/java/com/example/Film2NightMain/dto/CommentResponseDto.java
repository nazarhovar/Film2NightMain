package com.example.Film2NightMain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentResponseDto {
    private String author;
    private LocalDateTime createdAt;
    private String text;
}
