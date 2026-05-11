package com.example.fashion_db.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String id;
    String title;
    String slug;
    String label;
    String description;
    Long price;
    Boolean active;
    Boolean featured;
    LocalDateTime created_at;
    String thumbnail;
    List<String> images;
    String category_product_id;
}
