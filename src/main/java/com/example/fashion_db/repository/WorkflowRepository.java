package com.example.fashion_db.repository;

import com.example.fashion_db.entity.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, String> {
    List<Workflow> findByService_IdOrderByNoAsc(String serviceId);
}
