package com.example.fashion_db.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
    SeasonResponse season;
    CategoryCollectionResponse categoryCollection;
    ArtistResponse artist;
    List<ProductResponse> products;
}