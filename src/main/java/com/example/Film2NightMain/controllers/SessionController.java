package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.dto.SessionDto;
import com.example.Film2NightMain.dto.SessionUpdateDto;
import com.example.Film2NightMain.entities.Session;
import com.example.Film2NightMain.services.impl.SessionServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SessionController {
    private final SessionServiceImpl sessionServiceImpl;

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/session/create")
    public ResponseEntity<Session> createSession(@RequestBody SessionDto sessionDto) {
        try {
            Session session = sessionServiceImpl.createSession(sessionDto);
            return ResponseEntity.ok(session);
        } catch (Exception e) {
            throw new RuntimeException("Error creating session", e);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/session/update/{sessionId}")
    public ResponseEntity<Session> updateSession(@PathVariable Long sessionId, @RequestBody SessionUpdateDto sessionUpdateDto) {
        try {
            sessionUpdateDto.setSessionId(sessionId);
            Session session = sessionServiceImpl.updateSession(sessionUpdateDto);
            return ResponseEntity.ok(session);
        } catch (Exception e) {
            throw new RuntimeException("Error updating session", e);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @DeleteMapping("/session/cancel/{sessionId}")
    public void cancelSession(@PathVariable Long sessionId) {
        try {
            sessionServiceImpl.cancelSession(sessionId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting session", e);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PostMapping("/session/addUser/{sessionId}/{userId}")
    public ResponseEntity<Session> addUserToSession(@PathVariable Long sessionId, @PathVariable Long userId) {
        try {
            Session session = sessionServiceImpl.addUserToSession(sessionId, userId);
            return ResponseEntity.ok(session);
        } catch (Exception e) {
            throw new RuntimeException("Error adding user to session", e);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PostMapping("/session/removeUser/{sessionId}/{userId}")
    public ResponseEntity<Session> removeUserFromSession(@PathVariable Long sessionId, @PathVariable Long userId) {
        try {
            Session session = sessionServiceImpl.removeUserFromSession(sessionId, userId);
            return ResponseEntity.ok(session);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user from session", e);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/session/allSessions")
    public List<Session> getAllAvailableSessions() {
        try {
            List<Session> sessions = sessionServiceImpl.getAllAvailableSessions();
            return sessions;
        } catch (Exception e) {
            throw new RuntimeException("Error getting list of available sessions", e);
        }
    }
}
