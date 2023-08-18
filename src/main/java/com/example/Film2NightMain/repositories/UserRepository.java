package com.example.Film2NightMain.repositories;

import com.example.Film2NightMain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("SELECT DISTINCT u FROM User u JOIN Session s ON u = s.creator " +
            "WHERE s.startTime >= :startDateTime AND s.startTime <= :endDateTime AND u.isBlocked = false")
    List<User> findActiveUsers(@Param("startDateTime") LocalDateTime startDateTime,
                               @Param("endDateTime") LocalDateTime endDateTime);

}
