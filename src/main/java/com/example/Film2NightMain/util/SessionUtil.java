package com.example.Film2NightMain.util;

import com.example.Film2NightMain.entities.Session;
import com.example.Film2NightMain.entities.User;
import com.example.Film2NightMain.repositories.SessionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;



public class SessionUtil {

    public static void validateUserNotInSession(Session session, User user) {
        if (session.getUsers().contains(user)) {
            throw new IllegalArgumentException("User is already in the session.");
        }
    }

    public static void validateSessionNotCanceled(Session session) {
        if (session.getIsCanceled()) {
            throw new RuntimeException("Session is canceled");
        }
    }

    public static void validateSessionNotFull(Session session) {
        if (session.getVisitorCount() >= session.getMaxVisitorCount()) {
            throw new RuntimeException("Session is already full");
        }
    }

    public static Session getSessionById(SessionRepository sessionRepository, Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Session not found"));
    }
}
