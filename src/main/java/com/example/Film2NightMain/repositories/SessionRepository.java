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
    @Query("select s from Session s where s.isCanceled = false")
    Page<Session> findAllAvailableSessions(Pageable pageable);

    @Query("select s from Session s where s.startTime > :yearBefore")
    List<Session> findAllSessionsForYear(@Param("yearBefore") LocalDateTime yearBefore);

    @Query(value = "SELECT COUNT(*) FROM session s WHERE EXTRACT(YEAR FROM s.start_time) = :year " +
            "AND EXTRACT(MONTH FROM s.start_time) = :month AND EXTRACT(DAY FROM s.start_time) = :day", nativeQuery = true)
    Long countActiveSessionsForDay(@Param("year") int year, @Param("month") int month, @Param("day") int day);

    @Query(value = "SELECT COUNT(*) FROM session s WHERE EXTRACT(YEAR FROM s.start_time) = :year " +
            "AND EXTRACT(MONTH FROM s.start_time) = :month", nativeQuery = true)
    Long countActiveSessionsForMonth(@Param("year") int year, @Param("month") int month);

    @Query(value = "SELECT COUNT(*) FROM session s WHERE EXTRACT(YEAR FROM s.start_time) = :year " +
            "AND EXTRACT(WEEK FROM s.start_time) = :week", nativeQuery = true)
    Long countActiveSessionsForWeek(@Param("year") int year, @Param("week") int week);

    @Query(value = "SELECT COUNT(*) FROM session s WHERE EXTRACT(YEAR FROM s.start_time) = :year", nativeQuery = true)
    Long countActiveSessionsForYear(@Param("year") int year);

    @Query("SELECT new com.example.Film2NightMain.dto.RatingSessionDto(session.id, AVG(rating.rate)) " +
            "FROM Session session " +
            "JOIN session.rates rating " +
            "WHERE session.startTime BETWEEN :begin AND :end " +
            "GROUP BY session.id " +
            "ORDER BY AVG(rating.rate)")
    List<RatingSessionDto> getSessionByRatingScore(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);

}
