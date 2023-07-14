package com.example.Film2NightMain.repositories;

import com.example.Film2NightMain.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
