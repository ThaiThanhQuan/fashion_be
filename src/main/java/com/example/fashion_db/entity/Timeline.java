package com.example.fashion_db.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "timeline")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Timeline {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String label;
    String value;

    @ManyToOne
    @JoinColumn(name = "service_id")
    Service service;
}