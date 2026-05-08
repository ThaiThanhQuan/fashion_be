package com.example.fashion_db.repository;

import com.example.fashion_db.entity.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PricingRepository extends JpaRepository<Pricing, String> {
    List<Pricing> findByService_Id(String serviceId);
}
