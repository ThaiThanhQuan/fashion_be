package com.example.fashion_db.repository;

import com.example.fashion_db.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, String> {
    boolean existsByEmail(String email);
}
