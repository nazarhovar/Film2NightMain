package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.dto.SessionDto;
import com.example.Film2NightMain.dto.SessionUpdateDto;
import com.example.Film2NightMain.entities.Session;
import com.example.Film2NightMain.services.impl.SessionServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = "Сеансы")
public class SessionController {
    private final SessionServiceImpl sessionServiceImpl;

    @ApiOperation(value = "Создать сеанс")
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

    @ApiOperation(value = "Обновить сеанс по id")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/session/update/{sessionId}")
    public ResponseEntity<Session> updateSession(@PathVariable Long sessionId,
                                                 @RequestBody SessionUpdateDto sessionUpdateDto) {
        return ResponseEntity.ok().body(sessionServiceImpl.updateSession(sessionUpdateDto, sessionId));
    }

    @ApiOperation(value = "Удалить сеанс по id")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @DeleteMapping("/session/cancel/{sessionId}")
    public void cancelSession(@PathVariable Long sessionId) {
        try {
            sessionServiceImpl.cancelSession(sessionId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting session", e);
        }
    }

    @ApiOperation(value = "Добавить юзера к сеансу по id")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PostMapping("/session/addUser/{sessionId}/{userId}")
    public ResponseEntity<Session> addUserToSession(@PathVariable Long sessionId,
                                                    @PathVariable Long userId) {
        return ResponseEntity.ok().body(sessionServiceImpl.addUserToSession(sessionId, userId));
    }

    @ApiOperation(value = "Удалить юзера из сеанса по id")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PostMapping("/session/removeUser/{sessionId}/{userId}")
    public ResponseEntity<Session> removeUserFromSession(@PathVariable Long sessionId,
                                                         @PathVariable Long userId) {
        return ResponseEntity.ok().body(sessionServiceImpl.removeUserFromSession(sessionId, userId));
    }

    @ApiOperation(value = "Посмотреть все сеансы")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/session/allSessions")
    public Page<Session> getAllAvailableSessions(
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return sessionServiceImpl.getAllAvailableSessions(pageable);
        } catch (Exception e) {
            throw new RuntimeException("Error getting list of available sessions", e);
        }
    }
}
