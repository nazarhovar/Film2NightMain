package com.example.Film2NightMain.services.impl;

import com.example.Film2NightMain.dto.*;
import com.example.Film2NightMain.entities.Film;
import com.example.Film2NightMain.entities.Session;
import com.example.Film2NightMain.entities.User;
import com.example.Film2NightMain.repositories.SessionRepository;
import com.example.Film2NightMain.services.FilmService;
import com.example.Film2NightMain.services.SessionService;
import com.example.Film2NightMain.services.UserService;
import com.example.Film2NightMain.util.SessionUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@Service
public class SessionServiceImpl implements SessionService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SessionServiceImpl.class);
    private static final int MAX_VISITOR_COUNT = 10;

    private final SessionRepository sessionRepository;
    private final FilmService filmService;
    private final UserService userService;

    public SessionServiceImpl(SessionRepository sessionRepository, FilmService filmService, UserService userService) {
        this.sessionRepository = sessionRepository;
        this.filmService = filmService;
        this.userService = userService;
    }

    public Session createSession(SessionDto sessionDto) {
        Session session = new Session();
        session.setIsCanceled(false);
        Film film = filmService.findFilmById(sessionDto.getFilmId());
        session.setFilmId(film);
        session.setVisitorCount(1);
        session.setMaxVisitorCount(sessionDto.getMaxVisitorCount() > 0 ? sessionDto.getMaxVisitorCount() : MAX_VISITOR_COUNT);
        session.setStartTime(sessionDto.getStartTime());

        User creator = userService.getUserIdFromSecurityContext();
        session.setCreator(creator);

        List<User> users = new ArrayList<>();
        users.add(creator);
        session.setUsers(users);

        Session createdSession = sessionRepository.save(session);

        log.info("Session created: {} by user {}", createdSession.getId(), creator.getUsername());

        return createdSession;
    }

    public void cancelSession(Long sessionId) {
        Session session = SessionUtil.getSessionById(sessionRepository, sessionId);
        session.setIsCanceled(true);
        sessionRepository.save(session);
        log.info("Session canceled: {}", sessionId);
    }

    public Session joinSession(Long sessionId) {
        User user = userService.getUserIdFromSecurityContext();
        Session session = SessionUtil.getSessionById(sessionRepository, sessionId);

        SessionUtil.validateSessionNotCanceled(session);
        SessionUtil.validateSessionNotFull(session);
        SessionUtil.validateUserNotInSession(session, user);

        session.getUsers().add(user);
        session.setVisitorCount(session.getVisitorCount() + 1);

        return sessionRepository.save(session);
    }

    @Override
    public Session findSessionById(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Session not found"));
    }

    @Override
    public Session saveSession(Session session) {
        return sessionRepository.save(session);
    }

    public Page<Session> getAllAvailableSessions(Pageable pageable) {
        return sessionRepository.findAllAvailableSessions(pageable);
    }

    public List<Session> findAllSessionForYear() {
        LocalDateTime yearBefore = LocalDateTime.now().minusYears(1);
        log.info("All sessions for last year");
        return sessionRepository.findAllSessionsForYear(yearBefore);
    }

    public Long countActiveSessionsForDay(int year, int month, int day) {
        return sessionRepository.countActiveSessionsForDay(year, month, day);
    }

    public Long countActiveSessionsForMonth(int year, int month) {
        return sessionRepository.countActiveSessionsForMonth(year, month);
    }

    public Long countActiveSessionsForWeek(int year, int week) {
        return sessionRepository.countActiveSessionsForWeek(year, week);
    }

    public Long countActiveSessionsForYear(int year) {
        return sessionRepository.countActiveSessionsForYear(year);
    }

    public List<RatingSessionDto> getSessionByScoreRating(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime dateBegin = yearMonth.atDay(1).atTime(0, 0, 0);
        LocalDateTime dateEnd = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<RatingSessionDto> scoreAverageDtos = sessionRepository.getSessionByRatingScore(dateBegin, dateEnd);

        return scoreAverageDtos;
    }

}
