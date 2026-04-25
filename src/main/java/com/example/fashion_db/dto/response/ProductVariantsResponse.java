package com.example.fashion_db.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantsResponse {
    String id;
    String size;
    Integer stock;
    String product_id;
}
