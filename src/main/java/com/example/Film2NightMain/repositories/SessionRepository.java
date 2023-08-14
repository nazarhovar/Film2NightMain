package com.example.Film2NightMain.repositories;

import com.example.Film2NightMain.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    @Query(value = "SELECT * FROM session WHERE is_canceled = false", nativeQuery = true)
    List<Session> findAllAvailableSessions();
}
