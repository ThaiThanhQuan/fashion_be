package com.example.fashion_db.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollectionResponse {
    String id;
    String title;
    String slug;
    String year;
    Long price;
    String designIdeas;
    String description;
    String thumbnail;
    String seasonId;
    String categoryCollectionId;
    String artistId;
}