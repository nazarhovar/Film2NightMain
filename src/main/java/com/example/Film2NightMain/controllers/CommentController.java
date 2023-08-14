package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.dto.CommentDto;
import com.example.Film2NightMain.entities.Comment;
import com.example.Film2NightMain.services.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = "Комментарии")
public class CommentController {
    private final CommentService commentService;

    @ApiOperation(value = "Добавить комментарий к сеансу")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/comment/add")
    public ResponseEntity<Comment> addCommentToSession(@RequestBody CommentDto commentDto) {
        return ResponseEntity.ok().body(commentService.addCommentToSession(commentDto));
    }

    @ApiOperation(value = "Посмотреть все комментарии сеанса")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @GetMapping("/comment/{sessionId}")
    public ResponseEntity<List<String>> getCommentsForSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok().body(commentService.getCommentsForSession(sessionId));
    }
}
