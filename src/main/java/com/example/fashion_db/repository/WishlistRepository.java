package com.example.fashion_db.repository;

import com.example.fashion_db.entity.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, String> {
    Page<Wishlist> findByUser_Id(String userId, Pageable pageable);
    boolean existsByUser_IdAndProduct_Id(String userId, String productId);
    void deleteByUser_IdAndProduct_Id(String userId, String productId);
}