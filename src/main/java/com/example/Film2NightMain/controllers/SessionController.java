package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.dto.SessionDto;
import com.example.Film2NightMain.dto.SessionUpdateDto;
import com.example.Film2NightMain.entities.Session;
import com.example.Film2NightMain.services.impl.SessionServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SessionController {
    private final SessionServiceImpl sessionServiceImpl;

    @PostMapping("/session/create")
    public ResponseEntity<Session> createSession(@RequestBody SessionDto sessionDto) {
        return ResponseEntity.ok(sessionServiceImpl.createSession(sessionDto));
    }

    @PostMapping("/session/update/{sessionId}")
    public ResponseEntity<Session> updateSession(@PathVariable Long sessionId, @RequestBody SessionUpdateDto sessionUpdateDto) {
        // User должен быть создателем
        sessionUpdateDto.setSessionId(sessionId);
        return ResponseEntity.ok(sessionServiceImpl.updateSession(sessionUpdateDto));
    }

    @DeleteMapping("/session/cancel/{sessionId}")
    public void cancelSession(@PathVariable Long sessionId) {
        // User должен быть создателем
        sessionServiceImpl.cancelSession(sessionId);
    }

    @PostMapping("/session/addUser/{sessionId}/{userId}")
    public ResponseEntity<Session> addUserToSession(@PathVariable Long sessionId, @PathVariable Long userId) {
        return ResponseEntity.ok(sessionServiceImpl.addUserToSession(sessionId, userId));
    }

    @PostMapping("/session/removeUser/{sessionId}/{userId}")
    public ResponseEntity<Session> removeUserFromSession(@PathVariable Long sessionId, @PathVariable Long userId) {
        return ResponseEntity.ok(sessionServiceImpl.removeUserFromSession(sessionId, userId));
    }

    @GetMapping("/session/allSessions")
    public List<Session> getAllAvailableSessions() {
        return sessionServiceImpl.getAllAvailableSessions();
    }
}
