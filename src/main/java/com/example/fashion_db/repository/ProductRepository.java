package com.example.fashion_db.repository;

import com.example.fashion_db.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> , JpaSpecificationExecutor<Product> {
    boolean existsByTitle(String title);

    Page<Product> findByCategory_Id(String categoryId, Pageable pageable);
    Page<Product> findByActive(boolean active, Pageable pageable);
    Optional<Product> findBySlug(String slug);

    Page<Product> findByFeatured(boolean featured, Pageable pageable);
    Page<Product> findByPriceBetween(Long minPrice, Long maxPrice, Pageable pageable);

    Page<Product> findByCategory_IdAndIdNot(String categoryId, String productId, Pageable pageable);
    List<Product> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String title, String description);

    List<Product> findTop5ByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String title,
            String description
    );

}
