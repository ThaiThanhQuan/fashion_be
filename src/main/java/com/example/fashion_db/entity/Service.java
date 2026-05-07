package com.example.fashion_db.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "service")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true)
    String title;

    @Column(unique = true)
    String slug;

    String subTitle;
    String description;
    String detailDescription;
    String badge;
    String price;
    String thumbnail;
    String featureValue;
    String featureLabel;
    String quote;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    Artist artist;
}