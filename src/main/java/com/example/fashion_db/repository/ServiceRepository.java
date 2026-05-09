package com.example.fashion_db.repository;

import com.example.fashion_db.entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, String> {
    boolean existsByTitle(String title);
    Optional<Service> findBySlug(String slug);
    Page<Service> findAllByOrderByCreatedAtDesc(Pageable pageable);
}