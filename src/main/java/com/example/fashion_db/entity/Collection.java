package com.example.fashion_db.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "collections")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true)
    String title;

    @Column(unique = true)
    String slug;

    String year;
    Long price;
    String designIdeas;
    String description;
    String thumbnail;

    @ManyToOne
    @JoinColumn(name = "season_id")
    Season season;

    @ManyToOne
    @JoinColumn(name = "category_collection_id")
    CategoryCollection categoryCollection;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    Artist artist;


    @ManyToMany
    @JoinTable(
            name = "collection_products",           // tên bảng trung gian
            joinColumns = @JoinColumn(name = "collection_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    List<Product> products = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;
}