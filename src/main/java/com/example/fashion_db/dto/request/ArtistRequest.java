package com.example.fashion_db.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArtistRequest {
    String name;
    String feature;
    String description;
    String experience;
    String expertise;
    MultipartFile thumbnail;
}
