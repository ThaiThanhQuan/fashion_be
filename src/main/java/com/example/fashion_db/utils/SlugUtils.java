package com.example.fashion_db.utils;

import lombok.NoArgsConstructor;

import java.text.Normalizer;

@NoArgsConstructor
public class SlugUtils {

    public static String generateSlug(String title) {
        String normalized = Normalizer.normalize(title, Normalizer.Form.NFD);
        return normalized
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("đ", "d").replaceAll("Đ", "d")
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "")
                .trim();
    }
}
