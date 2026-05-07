package com.example.fashion_db.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceRequest {
    String title;
    String subTitle;
    String description;
    String detailDescription;
    String badge;
    String price;
    String featureValue;
    String featureLabel;
    String quote;
    String artistId;
    MultipartFile thumbnail;
}