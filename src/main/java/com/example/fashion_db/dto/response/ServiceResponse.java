package com.example.fashion_db.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceResponse {
    String id;
    String title;
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
    String artistId;
}