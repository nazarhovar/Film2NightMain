package com.example.Film2NightMain.services.impl;

import com.example.Film2NightMain.dto.CommentDto;
import com.example.Film2NightMain.dto.CommentResponseDto;
import com.example.Film2NightMain.entities.Comment;
import com.example.Film2NightMain.entities.Session;
import com.example.Film2NightMain.entities.User;
import com.example.Film2NightMain.repositories.CommentRepository;
import com.example.Film2NightMain.services.CommentService;
import com.example.Film2NightMain.services.SessionService;
import com.example.Film2NightMain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final SessionService sessionService;
    private final UserService userService;

    @Override
    public Comment addCommentToSession(CommentDto commentDto) {
        Session session = sessionService.findSessionById(commentDto.getSessionId());
        User user = userService.getUserIdFromSecurityContext();

        Comment newComment = new Comment();
        newComment.setSession(session);
        newComment.setUser(user);
        newComment.setText(commentDto.getCommentText());
        newComment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(newComment);
    }

    @Override
    public List<CommentResponseDto> getCommentsForSession(Long sessionId) {
        Session session = sessionService.findSessionById(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Session not found");
        }

        List<Comment> comments = commentRepository.findAllBySessionId(sessionId);
        List<CommentResponseDto> commentResponses = new ArrayList<>();

        for (Comment comment : comments) {
            String author = comment.getUser().getUsername();
            LocalDateTime createdAt = comment.getCreatedAt();
            String text = comment.getText();

            CommentResponseDto commentResponse = new CommentResponseDto(author, createdAt, text);
            commentResponses.add(commentResponse);
        }

        return commentResponses;
    }
}
