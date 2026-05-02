package com.example.fashion_db.repository;

import com.example.fashion_db.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, String> {
    boolean existsByName(String name);
}