package com.example.Film2NightMain.services;

import com.example.Film2NightMain.dto.CommentDto;
import com.example.Film2NightMain.dto.CommentResponseDto;
import com.example.Film2NightMain.entities.Comment;

import java.util.List;

public interface CommentService {
    Comment addCommentToSession(CommentDto commentDto);

    List<CommentResponseDto> getCommentsForSession(Long sessionId);
}
