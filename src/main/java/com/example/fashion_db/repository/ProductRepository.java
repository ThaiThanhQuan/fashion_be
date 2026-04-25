package com.example.fashion_db.repository;

import com.example.fashion_db.entity.Address;
import com.example.fashion_db.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    boolean existsByTitle(String title);

    List<Product> findByCategory_Id(String categoryId);
    List<Product> findByActive(boolean active);
    Optional<Product> findBySlug(String slug);

    List<Product> findAllByOrderByCreatedAtDesc();
    List<Product> findAllByOrderByPriceAsc();
    List<Product> findAllByOrderByPriceDesc();

    List<Product> findByFeatured(boolean featured);

}
