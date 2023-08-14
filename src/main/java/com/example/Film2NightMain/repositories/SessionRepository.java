package com.example.Film2NightMain.repositories;

import com.example.Film2NightMain.dto.RatingSessionDto;
import com.example.Film2NightMain.entities.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    @Query(value = "SELECT * FROM session WHERE is_canceled = false", nativeQuery = true)
    Page<Session> findAllAvailableSessions(Pageable pageable);

    @Query("select s from Session s where s.startTime > :yearBefore")
    List<Session> findAllSessionsForYear(@Param("yearBefore") LocalDateTime yearBefore);

    @Query("SELECT COUNT(s) FROM Session s WHERE FUNCTION('YEAR', s.startTime) = :year " +
            "AND FUNCTION('MONTH', s.startTime) = :month AND FUNCTION('DAY', s.startTime) = :day")
    Long countActiveSessionsForDay(@Param("year") int year, @Param("month") int month, @Param("day") int day);

    @Query("SELECT COUNT(s) FROM Session s WHERE FUNCTION('YEAR', s.startTime) = :year " +
            "AND FUNCTION('MONTH', s.startTime) = :month")
    Long countActiveSessionsForMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT COUNT(s) FROM Session s WHERE FUNCTION('YEAR', s.startTime) = :year " +
            "AND FUNCTION('WEEK', s.startTime) = :week")
    Long countActiveSessionsForWeek(@Param("year") int year, @Param("week") int week);

    @Query("SELECT COUNT(s) FROM Session s WHERE FUNCTION('YEAR', s.startTime) = :year")
    Long countActiveSessionsForYear(@Param("year") int year);

    @Query("SELECT new com.example.Film2NightMain.dto.RatingSessionDto(session.id, AVG(rating.rate)) " +
            "FROM Session session " +
            "JOIN session.rates rating " +
            "WHERE session.startTime BETWEEN :begin AND :end " +
            "GROUP BY session.id " +
            "ORDER BY AVG(rating.rate)")
    List<RatingSessionDto> getSessionByRatingScore(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);

}
