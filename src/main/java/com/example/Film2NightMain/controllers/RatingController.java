package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.dto.EntityMapper;
import com.example.Film2NightMain.dto.RatingDto;
import com.example.Film2NightMain.dto.SessionResponseDto;
import com.example.Film2NightMain.services.RatingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Api(tags = "Rating")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @ApiOperation(value = "Rate the session")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/rating/add")
    public ResponseEntity<SessionResponseDto> rateSession(@RequestBody RatingDto ratingDto) {
        return ResponseEntity.ok(EntityMapper.toSessionDto(ratingService.rateSession(ratingDto)));
    }
}
