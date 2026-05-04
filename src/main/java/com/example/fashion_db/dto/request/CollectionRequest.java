package com.example.fashion_db.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollectionRequest {
    String title;
    String year;
    Long price;
    String designIdeas;
    String description;
    String seasonId;
    String categoryCollectionId;
    String artistId;
    MultipartFile thumbnail;
}
