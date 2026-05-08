package com.example.fashion_db.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "workflow")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Workflow {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String no;
    String title;
    String content;

    @ManyToOne
    @JoinColumn(name = "service_id")
    Service service;
}