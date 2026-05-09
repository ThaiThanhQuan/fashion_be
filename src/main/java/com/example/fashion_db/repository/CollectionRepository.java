package com.example.fashion_db.repository;

import com.example.fashion_db.entity.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, String>,JpaSpecificationExecutor<Collection> {
    boolean existsByTitle(String title);
    Optional<Collection> findBySlug(String slug);
    Page<Collection> findBySeason_Id(String seasonId, Pageable pageable);
    Page<Collection> findByCategoryCollection_Id(String categoryCollectionId, Pageable pageable);
    Page<Collection> findByArtist_Id(String artistId, Pageable pageable);
    Page<Collection> findByYear(String year, Pageable pageable);
    Page<Collection> findAllByOrderByCreatedAtDesc(Pageable pageable);
}