package com.example.fashion_db.repository;

import com.example.fashion_db.entity.Order;
import com.example.fashion_db.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    Page<Order> findByUser_Id(String userId, Pageable pageable);
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
}