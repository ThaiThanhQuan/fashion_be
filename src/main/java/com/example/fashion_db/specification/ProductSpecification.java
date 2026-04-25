package com.example.fashion_db.specification;

import com.example.fashion_db.entity.Product;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    // (root, query, cb) là 3 tham số cố định của Specification
    // root = đại diện cho bảng products (để lấy column)
    // query = câu query đang build
    // cb = CriteriaBuilder, dùng để tạo điều kiện WHERE

    public static Specification<Product> hasCategory(String categoryId) {
        return (root, query, cb) ->
                categoryId == null ? null : cb.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Product> hasActive(Boolean active) {
        return (root, query, cb) ->
                active == null ? null : cb.equal(root.get("active"), active);
    }

    public static Specification<Product> hasFeatured(Boolean featured) {
        return (root, query, cb) ->
                featured == null ? null : cb.equal(root.get("featured"), featured);
    }

    public static Specification<Product> hasPriceBetween(Long minPrice, Long maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null) return null;
            if (minPrice == null) return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
            if (maxPrice == null) return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
            return cb.between(root.get("price"), minPrice, maxPrice);
        };
    }

    public static Specification<Product> hasSize(String size) {
        return (root, query, cb) -> {
            if (size == null) return null;
            // Join sang product_variants
            var variants = root.join("variants", JoinType.INNER);
            return cb.equal(variants.get("size"), size);
        };
    }
}
