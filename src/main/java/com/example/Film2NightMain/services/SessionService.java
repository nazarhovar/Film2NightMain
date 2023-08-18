package com.example.Film2NightMain.services;

import com.example.Film2NightMain.dto.SessionDto;
import com.example.Film2NightMain.dto.SessionUpdateDto;
import com.example.Film2NightMain.entities.Session;

public interface SessionService {
    Session createSession(SessionDto sessionDto);

    Session updateSession(SessionUpdateDto sessionUpdateDto, Long sessionId);

    void cancelSession(Long sessionId);

    Session addUserToSession(Long sessionId, Long userId);

    Session removeUserFromSession(Long sessionId, Long userId);

    Session findSessionById(Long sessionId);

    Session saveSession(Session session);
}
