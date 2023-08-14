package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.dto.RatingDto;
import com.example.Film2NightMain.entities.Session;
import com.example.Film2NightMain.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RatingController {
    private final RatingService ratingService;

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/rating/add")
    public ResponseEntity<Session> rateSession(@RequestBody RatingDto ratingDto) {
        return ResponseEntity.ok().body(ratingService.rateSession(ratingDto));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @GetMapping("/rating/{sessionId}")
    public ResponseEntity<Double> getSessionRating(@PathVariable Long sessionId) {
        return ResponseEntity.ok(ratingService.getSessionRating(sessionId));
    }
}
