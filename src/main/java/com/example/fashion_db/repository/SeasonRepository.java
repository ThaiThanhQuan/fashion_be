package com.example.fashion_db.repository;

import com.example.fashion_db.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonRepository extends JpaRepository<Season, String> {
    boolean existsByName(String name);
}