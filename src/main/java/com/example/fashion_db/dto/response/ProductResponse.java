package com.example.fashion_db.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

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


    String category_product_id;
}
