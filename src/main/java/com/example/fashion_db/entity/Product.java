package com.example.fashion_db.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true)
    String title;

    @Column(unique = true)
    String slug;

    String label;
    String description;
    Long price;
    boolean active;
    boolean featured;

    @ManyToOne
    @JoinColumn(name = "category_product_id")
    CategoriesProduct category;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    List<ProductVariants> variants;

    @CreationTimestamp
    LocalDateTime createdAt;
}
