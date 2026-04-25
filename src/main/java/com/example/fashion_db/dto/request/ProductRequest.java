package com.example.fashion_db.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    String title;
    String slug;
    String label;
    String description;
    Long price;
    Boolean active;
    Boolean featured;
    String category_product;
}
