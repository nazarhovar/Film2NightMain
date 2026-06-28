package com.example.Film2NightMain.repositories;

import com.example.Film2NightMain.entities.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FilmRepository extends JpaRepository<Film, Long> {

    @Query("SELECT f FROM Film f WHERE LOWER(f.name_origin) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Film> searchByName(@Param("search") String search, Pageable pageable);

    @Query(value = "SELECT * FROM film WHERE kinopoisk_id IN (SELECT id FROM top250)",
            countQuery = "SELECT COUNT(*) FROM film WHERE kinopoisk_id IN (SELECT id FROM top250)",
            nativeQuery = true)
    Page<Film> findTop250(Pageable pageable);
}
