package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.dto.CommentDto;
import com.example.Film2NightMain.entities.Comment;
import com.example.Film2NightMain.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/comment/add")
    public ResponseEntity<Comment> addCommentToSession(@RequestBody CommentDto commentDto) {
        return ResponseEntity.ok().body(commentService.addCommentToSession(commentDto));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @GetMapping("/comment/{sessionId}")
    public ResponseEntity<List<String>> getCommentsForSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok().body(commentService.getCommentsForSession(sessionId));
    }
}
