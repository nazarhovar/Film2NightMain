package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.dto.EntityMapper;
import com.example.Film2NightMain.dto.RatingSessionDto;
import com.example.Film2NightMain.dto.SessionResponseDto;
import com.example.Film2NightMain.services.UserService;
import com.example.Film2NightMain.services.SessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Api(tags = "Statistics")
public class AnalyticController {
    private final SessionService sessionService;
    private final UserService userService;

    public AnalyticController(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @ApiOperation(value = "All sessions for last year")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @GetMapping("/session/lastYear")
    public ResponseEntity<List<SessionResponseDto>> getAllSessionForYear() {
        List<SessionResponseDto> sessions = sessionService.findAllSessionForYear().stream()
                .map(EntityMapper::toSessionDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(sessions);
    }

    @ApiOperation(value = "Number of active sessions per year")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @GetMapping("/session/count/active/{year}")
    public ResponseEntity getActiveSessionsForYear(@PathVariable(name = "year") int year) {
        return ResponseEntity.ok().body(sessionService.countActiveSessionsForYear(year));
    }

    @ApiOperation(value = "Number of active sessions per month")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @GetMapping("/session/count/active/month/{year}/{month}")
    public ResponseEntity getSessionsForMonth(@PathVariable(name = "year") int year,
                                              @PathVariable(name = "month") int month) {
        return ResponseEntity.ok().body(sessionService.countActiveSessionsForMonth(year, month));
    }

    @ApiOperation(value = "Number of active sessions per week")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @GetMapping("/sessions/count/active/week/{year}/{week}")
    public ResponseEntity getSessionsForWeek(@PathVariable(name = "year") int year,
                                             @PathVariable(name = "week") int week) {
        return ResponseEntity.ok().body(sessionService.countActiveSessionsForWeek(year, week));
    }

    @ApiOperation(value = "Number of active sessions per day")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @GetMapping("/sessions/count/active/day/{year}/{month}/{day}")
    public ResponseEntity getSessionsForDay(@PathVariable(name = "year") int year,
                                            @PathVariable(name = "month") int month,
                                            @PathVariable(name = "day") int day) {
        return ResponseEntity.ok().body(sessionService.countActiveSessionsForDay(year, month, day));
    }

    @ApiOperation(value = "Rating of sessions based on monthly ratings")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @GetMapping("/sessions/rating/{year}/{month}")
    public ResponseEntity<List<RatingSessionDto>> getSessionByScoreRating(
            @PathVariable(name = "year") int year,
            @PathVariable(name = "month") int month) {
        return ResponseEntity.ok().body(sessionService.getSessionByScoreRating(year, month));
    }

    @ApiOperation(value = "Number of active users per year")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @GetMapping("/users/active/year/{year}")
    public ResponseEntity getActiveUserForYear(@PathVariable(name = "year") Integer year) {
        return ResponseEntity.ok().body(userService.countActiveUsersForYear(year));
    }

    @ApiOperation(value = "Number of active users per month")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @GetMapping("/users/active/month/{year}/{month}")
    public ResponseEntity getActiveUserForMonth(@PathVariable(name = "year") Integer year,
                                                @PathVariable(name = "month") Integer month) {
        return ResponseEntity.ok().body(userService.countActiveUsersForMonth(year, month));
    }

    @ApiOperation(value = "Number of active users per week")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @GetMapping("/users/active/week/{year}/{month}/{week}")
    public ResponseEntity getActiveUserForWeek(@PathVariable(name = "year") Integer year,
                                               @PathVariable(name = "month") Integer month,
                                               @PathVariable(name = "week") Integer week) {
        return ResponseEntity.ok().body(userService.countActiveUsersForWeek(year, month, week));
    }

    @ApiOperation(value = "Number of active users per day")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @GetMapping("/users/active/day/{year}/{month}/{day}")
    public ResponseEntity getActiveUserForDay(@PathVariable(name = "year") Integer year,
                                              @PathVariable(name = "month") Integer month,
                                              @PathVariable(name = "day") Integer day) {
        return ResponseEntity.ok().body(userService.countActiveUsersForDay(year, month, day));
    }
}
