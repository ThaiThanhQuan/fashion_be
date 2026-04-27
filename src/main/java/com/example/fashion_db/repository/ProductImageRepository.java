package com.example.fashion_db.repository;

import com.example.fashion_db.entity.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, String> {
    Page<ProductImage> findByProduct_Id(String productId, Pageable pageable);
    Optional<ProductImage> findByProduct_IdAndThumbnailTrue(String productId);
    boolean existsByProduct_IdAndThumbnailTrue(String productId);
}