package com.example.fashion_db.repository;

import com.example.fashion_db.entity.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, String> {
    boolean existsByUser_IdAndProduct_Id(String userId, String productId);
    void deleteByUser_IdAndProduct_Id(String userId, String productId);
    List<Wishlist> findByUser_Id(String userId);  // không có Pageable
}