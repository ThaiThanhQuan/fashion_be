package com.example.fashion_db.repository;

import com.example.fashion_db.entity.Highlight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HighlightRepository extends JpaRepository<Highlight, String> {
    List<Highlight> findByService_Id(String serviceId);
    void deleteByService_Id(String serviceId);
}