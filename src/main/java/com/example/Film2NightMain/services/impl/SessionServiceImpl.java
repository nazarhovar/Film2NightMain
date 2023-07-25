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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private static final int VISITOR_COUNT = 4;
    private static final int MAX_VISITOR_COUNT = 10;

    private static final Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);

    private final SessionRepository sessionRepository;
    private final FilmService filmService;
    private final UserService userService;

    public Session createSession(SessionDto sessionDto) {
        Session session = new Session();
        session.setIsCanceled(false);
        session.setFilmId(filmService.findFilmById(sessionDto.getFilmId()));
        session.setVisitorCount(VISITOR_COUNT);
        session.setMaxVisitorCount(MAX_VISITOR_COUNT);
        session.setStartTime(sessionDto.getStartTime());

        User creator = userService.findUserById(sessionDto.getCreatorId());
        session.setCreator(creator);

        List<User> users = SessionUtil.getUsers(sessionDto.getUsers(), userService);
        session.setUsers(users);

        Session createdSession = sessionRepository.save(session);

        logger.info("Session created: {}", createdSession.getId());

        return createdSession;
    }

    public Session updateSession(SessionUpdateDto sessionUpdateDto) {
        Long sessionId = sessionUpdateDto.getSessionId();
        Session session = SessionUtil.getSessionById(sessionRepository, sessionId);

        session.setFilmId(filmService.findFilmById(sessionUpdateDto.getFilmId()));
        session.setVisitorCount(sessionUpdateDto.getMaxVisitorCount());
        session.setStartTime(sessionUpdateDto.getStartTime());

        User creator = userService.findUserById(sessionUpdateDto.getCreatorId());
        session.setCreator(creator);

        List<User> users = SessionUtil.getUsers(sessionUpdateDto.getUsers(), userService);
        session.setUsers(users);

        Session updatedSession = sessionRepository.save(session);

        logger.info("Session updated: {}", updatedSession.getId());

        return updatedSession;
    }

    public void cancelSession(Long sessionId) {
        sessionRepository.deleteById(sessionId);
        logger.info("Session deleted: {}" , sessionId);
    }

    public Session addUserToSession(Long sessionId, Long userId) {
        User user = userService.findUserById(userId);
        Session session = SessionUtil.getSessionById(sessionRepository, sessionId);

        SessionUtil.validateSessionNotCanceled(session);
        SessionUtil.validateSessionNotFull(session);

        session.getUsers().add(user);
        session.setVisitorCount(session.getVisitorCount() + 1);

        return sessionRepository.save(session);
    }

    public Session removeUserFromSession(Long sessionId, Long userId) {
        User user = userService.findUserById(userId);
        Session session = SessionUtil.getSessionById(sessionRepository, sessionId);

        SessionUtil.validateSessionNotCanceled(session);
        SessionUtil.validateUserInSession(session, user);

        session.getUsers().remove(user);
        session.setVisitorCount(session.getVisitorCount() - 1);

        return sessionRepository.save(session);
    }

    public List<Session> getAllAvailableSessions() {
        List<Session> allSessions = sessionRepository.findAll();

        return allSessions.stream()
                .filter(SessionUtil::isSessionAvailable)
                .collect(Collectors.toList());
    }
}
