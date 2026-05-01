package com.example.fashion_db.repository;

import com.example.fashion_db.entity.CategoryCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryCollectionRepository extends JpaRepository<CategoryCollection, String> {
    boolean existsByName(String name);
}
