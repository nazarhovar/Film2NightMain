package com.example.Film2NightMain.util;

import com.example.Film2NightMain.entities.Session;
import com.example.Film2NightMain.entities.User;
import com.example.Film2NightMain.repositories.SessionRepository;
import com.example.Film2NightMain.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

public class SessionUtil {

    public static List<User> getUsers(List<Long> userIds, UserService userService) {
        List<User> users = new ArrayList<>();
        if (userIds != null && !userIds.isEmpty()) {
            for (Long userId : userIds) {
                User user = userService.findUserById(userId);
                users.add(user);
            }
        }

        return users;
    }

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

    public static void validateUserInSession(Session session, User user) {
        if (!session.getUsers().contains(user)) {
            throw new RuntimeException("User is not in the session");
        }
    }

    public static Session getSessionById(SessionRepository sessionRepository, Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Session not found"));
    }
}
