package com.example.Film2NightMain.repositories;

import com.example.Film2NightMain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
