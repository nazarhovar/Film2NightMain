package com.example.Film2NightMain.services.impl;

import com.example.Film2NightMain.dto.SessionDto;
import com.example.Film2NightMain.dto.SessionUpdateDto;
import com.example.Film2NightMain.entities.Session;
import com.example.Film2NightMain.entities.User;
import com.example.Film2NightMain.repositories.SessionRepository;
import com.example.Film2NightMain.services.FilmService;
import com.example.Film2NightMain.services.SessionService;
import com.example.Film2NightMain.services.UserService;
import com.example.Film2NightMain.util.SessionUtil;
import org.springframework.stereotype.Service;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final FilmService filmService;
    private final UserService userService;

    public Session createSession(SessionDto sessionDto) {
        Session session = new Session();
        session.setIsCanceled(false);
        session.setFilmId(filmService.findFilmById(sessionDto.getFilmId()));
        session.setVisitorCount(4);
        session.setStartTime(sessionDto.getStartTime());
        session.setMaxVisitorCount(10);

        User creator = userService.findUserById(sessionDto.getCreatorId());
        session.setCreator(creator);

        List<User> users = SessionUtil.getUsers(sessionDto.getUsers(), userService);
        session.setUsers(users);

        System.out.println("Session created.");
        return sessionRepository.save(session);
    }

    public Session updateSession(SessionUpdateDto sessionUpdateDto) {
        Session session = sessionRepository.findById(sessionUpdateDto.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session not found"));
        session.setFilmId(filmService.findFilmById(sessionUpdateDto.getFilmId()));
        session.setVisitorCount(sessionUpdateDto.getMaxVisitorCount());
        session.setStartTime(sessionUpdateDto.getStartTime());

        User creator = userService.findUserById(sessionUpdateDto.getCreatorId());
        session.setCreator(creator);

        List<User> users = SessionUtil.getUsers(sessionUpdateDto.getUsers(), userService);
        session.setUsers(users);

        System.out.println("Session updated.");
        return sessionRepository.save(session);
    }

    public void cancelSession(Long sessionId) {
        sessionRepository.deleteById(sessionId);
        System.out.println("Session deleted.");
    }

    public Session addUserToSession(Long sessionId, Long userId) {
        User user = userService.findUserById(userId);
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        SessionUtil.validateSessionNotCanceled(session);
        SessionUtil.validateSessionNotFull(session);

        session.getUsers().add(user);
        session.setVisitorCount(session.getVisitorCount() + 1);

        return sessionRepository.save(session);
    }

    public Session removeUserFromSession(Long sessionId, Long userId) {
        User user = userService.findUserById(userId);
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        SessionUtil.validateSessionNotCanceled(session);
        SessionUtil.validateUserInSession(session, user);

        session.getUsers().remove(user);
        session.setVisitorCount(session.getVisitorCount() - 1);

        return sessionRepository.save(session);
    }

    public List<Session> getAllAvailableSessions() {
        List<Session> allSessions = sessionRepository.findAll();
        List<Session> availableSessions = new ArrayList<>();

        for (Session session : allSessions) {
            if (SessionUtil.isSessionAvailable(session)) {
                availableSessions.add(session);
            }
        }

        return availableSessions;
    }
}
