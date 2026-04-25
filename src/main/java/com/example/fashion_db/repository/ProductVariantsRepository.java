package com.example.fashion_db.repository;

import com.example.fashion_db.entity.ProductVariants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantsRepository extends JpaRepository<ProductVariants, String> {
    Page<ProductVariants> findByProduct_Id(String productId, Pageable pageable);
    boolean existsByProduct_IdAndSizeAndIdNot(String productId, String size, String id);
    Page<ProductVariants> findBySize(String size, Pageable pageable);
}
