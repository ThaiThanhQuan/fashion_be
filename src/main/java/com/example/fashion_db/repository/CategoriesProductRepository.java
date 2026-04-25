package com.example.fashion_db.repository;

import com.example.fashion_db.entity.CategoriesProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriesProductRepository extends JpaRepository<CategoriesProduct, String> {
}
