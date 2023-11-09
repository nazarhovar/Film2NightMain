package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.dto.RatingSessionDto;
import com.example.Film2NightMain.entities.Session;
import com.example.Film2NightMain.services.UserService;
import com.example.Film2NightMain.services.impl.SessionServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = "Statistics")
public class AnalyticController {
    private final SessionServiceImpl sessionServiceImpl;
    private final UserService userService;

    @ApiOperation(value = "All sessions for last year")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @GetMapping("/session/lastYear")
    public ResponseEntity<List<Session>> getAllSessionForYear() {
        return ResponseEntity.ok().body(sessionServiceImpl.findAllSessionForYear());
    }

    @ApiOperation(value = "Number of active sessions per year")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @GetMapping("/session/count/active/{year}")
    public ResponseEntity getActiveSessionsForYear(@PathVariable(name = "year") int year) {
        return ResponseEntity.ok().body(sessionServiceImpl.countActiveSessionsForYear(year));
    }

    @ApiOperation(value = "Number of active sessions per month")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @GetMapping("/session/count/active/month/{year}/{month}")
    public ResponseEntity getSessionsForMonth(@PathVariable(name = "year") int year,
                                              @PathVariable(name = "month") int month) {
        return ResponseEntity.ok().body(sessionServiceImpl.countActiveSessionsForMonth(year, month));
    }

    @ApiOperation(value = "Number of active sessions per week")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @GetMapping("/sessions/count/active/week/{year}/{week}")
    public ResponseEntity getSessionsForWeek(@PathVariable(name = "year") int year,
                                             @PathVariable(name = "week") int week) {
        return ResponseEntity.ok().body(sessionServiceImpl.countActiveSessionsForWeek(year, week));
    }

    @ApiOperation(value = "Number of active sessions per day")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @GetMapping("/sessions/count/active/day/{year}/{month}/{day}")
    public ResponseEntity getSessionsForDay(@PathVariable(name = "year") int year,
                                            @PathVariable(name = "month") int month,
                                            @PathVariable(name = "day") int day) {
        return ResponseEntity.ok().body(sessionServiceImpl.countActiveSessionsForDay(year, month, day));
    }

    @ApiOperation(value = "Rating of sessions based on monthly ratings")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @GetMapping("/sessions/rating/{year}/{month}")
    public ResponseEntity<List<RatingSessionDto>> getSessionByScoreRating(
            @PathVariable(name = "year") int year,
            @PathVariable(name = "month") int month) {
        return ResponseEntity.ok().body(sessionServiceImpl.getSessionByScoreRating(year, month));
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
