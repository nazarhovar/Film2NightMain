package com.example.Film2NightMain.dto;

import lombok.Data;

@Data
public class CommentDto {
    private Long sessionId;
    private Long userId;
    private String commentText;
}
