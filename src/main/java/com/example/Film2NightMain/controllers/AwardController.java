package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.dto.AwardDto;
import com.example.Film2NightMain.services.AwardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = "Награды")
public class AwardController {
    public final AwardService awardService;

    @ApiOperation(value = "Присвоить награду")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PostMapping("/award/add")
    public ResponseEntity setAward(@RequestBody AwardDto awardDto) {
        return ResponseEntity.ok(awardService.setAwardToSession(awardDto));
    }
}
