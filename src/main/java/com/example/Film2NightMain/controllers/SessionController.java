package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.dto.EntityMapper;
import com.example.Film2NightMain.dto.SessionDto;
import com.example.Film2NightMain.dto.SessionResponseDto;
import com.example.Film2NightMain.entities.Session;
import com.example.Film2NightMain.services.SessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Api(tags = "Sessions")
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @ApiOperation(value = "Create session")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/session/create")
    public ResponseEntity<SessionResponseDto> createSession(@RequestBody SessionDto sessionDto) {
        Session session = sessionService.createSession(sessionDto);
        return ResponseEntity.ok(EntityMapper.toSessionDto(session));
    }

    @ApiOperation(value = "Delete session by id")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @DeleteMapping("/session/cancel/{sessionId}")
    public void cancelSession(@PathVariable Long sessionId) {
        sessionService.cancelSession(sessionId);
    }

    @ApiOperation(value = "Join a session directly (no bid)")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/session/join/{sessionId}")
    public ResponseEntity<SessionResponseDto> joinSession(@PathVariable Long sessionId) {
        Session session = sessionService.joinSession(sessionId);
        return ResponseEntity.ok(EntityMapper.toSessionDto(session));
    }

    @ApiOperation(value = "Get session for room")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<SessionResponseDto> getSessionById(@PathVariable Long sessionId) {
        return ResponseEntity.ok(EntityMapper.toSessionDto(sessionService.findSessionById(sessionId)));
    }

    @ApiOperation(value = "View all sessions")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @GetMapping("/session/allSessions")
    public Page<SessionResponseDto> getAllAvailableSessions(
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return sessionService.getAllAvailableSessions(pageable)
                .map(EntityMapper::toSessionDto);
    }
}
