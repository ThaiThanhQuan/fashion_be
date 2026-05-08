package com.example.fashion_db.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "pricing")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String label;
    String price;

    @ManyToOne
    @JoinColumn(name = "service_id")
    Service service;
}