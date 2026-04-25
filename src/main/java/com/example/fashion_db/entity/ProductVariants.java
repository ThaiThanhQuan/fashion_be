package com.example.fashion_db.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Entity
public class ProductVariants {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String size;
    Integer stock;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;
}
