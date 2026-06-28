package com.example.Film2NightMain.services;

import com.example.Film2NightMain.dto.RatingSessionDto;
import com.example.Film2NightMain.dto.SessionDto;
import com.example.Film2NightMain.entities.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SessionService {
    Session createSession(SessionDto sessionDto);

    void cancelSession(Long sessionId);

    Session findSessionById(Long sessionId);

    Session saveSession(Session session);

    Session joinSession(Long sessionId);

    Page<Session> getAllAvailableSessions(Pageable pageable);

    List<Session> findAllSessionForYear();

    Long countActiveSessionsForYear(int year);

    Long countActiveSessionsForMonth(int year, int month);

    Long countActiveSessionsForWeek(int year, int week);

    Long countActiveSessionsForDay(int year, int month, int day);

    List<RatingSessionDto> getSessionByScoreRating(int year, int month);
}
