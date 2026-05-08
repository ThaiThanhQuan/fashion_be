package com.example.fashion_db.repository;

import com.example.fashion_db.entity.Timeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimelineRepository extends JpaRepository<Timeline, String> {
    List<Timeline> findByService_Id(String serviceId);
}