package com.example.fashion_db.specification;

import com.example.fashion_db.entity.Collection;
import org.springframework.data.jpa.domain.Specification;

public class CollectionSpecification {

    public static Specification<Collection> hasSeason(String seasonId) {
        return (root, query, cb) ->
                seasonId == null ? null : cb.equal(root.get("season").get("id"), seasonId);
    }

    public static Specification<Collection> hasCategory(String categoryId) {
        return (root, query, cb) ->
                categoryId == null ? null : cb.equal(root.get("categoryCollection").get("id"), categoryId);
    }

    public static Specification<Collection> hasArtist(String artistId) {
        return (root, query, cb) ->
                artistId == null ? null : cb.equal(root.get("artist").get("id"), artistId);
    }

    public static Specification<Collection> hasYear(String year) {
        return (root, query, cb) ->
                year == null ? null : cb.equal(root.get("year"), year);
    }
}