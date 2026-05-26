package com.example.fashion_db.service;

import com.example.fashion_db.entity.ProductImage;
import com.example.fashion_db.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AIService {

    private final ProductRepository productRepository;
    private final ServiceRepository serviceRepository;
    private final ProductImageRepository productImageRepository;
    private final CollectionRepository collectionRepository;
    private final ArtistRepository artistRepository;

    @Value("${groq.api-key}")
    private String groqApiKey;

    public Map<String, Object> chat(String message) {
        String context = buildContext();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        Map<String, Object> body = Map.of(
                "model", "llama-3.3-70b-versatile",
                "max_tokens", 1000,
                "messages", List.of(
                        Map.of("role", "system", "content",
                                "Bạn là trợ lý thời trang của COUTURE.\n\n" +
                                        "Dữ liệu thực tế:\n" + context + "\n\n" +
                                        "Hướng dẫn:\n" +
                                        "- Trả lời tiếng Việt, lịch sự\n" +
                                        "- Ngắn gọn 2-4 câu\n" +
                                        "- Khi đề cập sản phẩm/collection/service, PHẢI ghi đúng tên trong dữ liệu\n" +
                                        "- Cuối reply, nếu có sản phẩm/collection/service liên quan, liệt kê ID dạng:\n" +
                                        "  [ITEMS:product:ID1,ID2|collection:ID3|service:ID4]\n" +
                                        "- Nếu không có item liên quan thì không cần ghi"
                        ),
                        Map.of("role", "user", "content", message)
                )
        );

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.groq.com/openai/v1/chat/completions",
                    new HttpEntity<>(body, headers),
                    Map.class
            );

            List<Map> choices = (List<Map>) response.getBody().get("choices");
            Map messageObj = (Map) choices.get(0).get("message");
            String fullReply = (String) messageObj.get("content");

            String reply = fullReply.replaceAll("\\[ITEMS:[^\\]]*]", "").trim();
            List<Map<String, String>> items = extractItems(fullReply);

            return Map.of("reply", reply, "items", items);

        } catch (Exception e) {
            return Map.of("reply", "Xin lỗi, có lỗi xảy ra!", "items", List.of());
        }
    }

    private String buildContext() {
        StringBuilder ctx = new StringBuilder();

        ctx.append("=== SẢN PHẨM ===\n");
        productRepository.findAll().stream().limit(20).forEach(p -> {
            String thumbnail = productImageRepository
                    .findByProduct_IdAndThumbnailTrue(p.getId())
                    .map(ProductImage::getImagePath)
                    .orElse("");
            ctx.append(String.format(
                    "ID:%s | Tên:%s | Giá:%,.0f VND | Danh mục:%s | Slug:%s | Ảnh:%s\n",
                    p.getId(), p.getTitle(), (double) p.getPrice(),
                    p.getCategory() != null ? p.getCategory().getName() : "N/A",
                    p.getSlug(), thumbnail
            ));
        });

        ctx.append("\n=== BỘ SƯU TẬP ===\n");
        collectionRepository.findAll().stream().limit(10).forEach(c ->
                ctx.append(String.format(
                        "ID:%s | Tên:%s | Năm:%s | Mùa:%s | Slug:%s | Ảnh:%s\n",
                        c.getId(), c.getTitle(), c.getYear(),
                        c.getSeason() != null ? c.getSeason().getName() : "N/A",
                        c.getSlug(), c.getThumbnail() != null ? c.getThumbnail() : ""
                ))
        );

        ctx.append("\n=== DỊCH VỤ ===\n");
        serviceRepository.findAll().forEach(s ->
                ctx.append(String.format(
                        "ID:%s | Tên:%s | Giá:%s | Mô tả:%s | Slug:%s | Ảnh:%s\n",
                        s.getId(), s.getTitle(), s.getPrice(),
                        s.getSubTitle(), s.getSlug(),
                        s.getThumbnail() != null ? s.getThumbnail() : ""
                ))
        );

        ctx.append("\n=== NGHỆ SĨ ===\n");
        artistRepository.findAll().forEach(a ->
                ctx.append(String.format(
                        "ID:%s | Tên:%s | Chuyên môn:%s | Ảnh:%s\n",
                        a.getId(), a.getName(), a.getExpertise(),
                        a.getThumbnail() != null ? a.getThumbnail() : ""
                ))
        );

        return ctx.toString();
    }

    private List<Map<String, String>> extractItems(String text) {
        List<Map<String, String>> items = new ArrayList<>();

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\[ITEMS:([^\\]]*)]");
        java.util.regex.Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) return items;

        for (String part : matcher.group(1).split("\\|")) {
            String[] typeAndIds = part.split(":");
            if (typeAndIds.length < 2) continue;
            String type = typeAndIds[0].trim();

            for (String id : typeAndIds[1].split(",")) {
                String trimId = id.trim();
                try {
                    switch (type) {
                        case "product" -> productRepository.findById(String.valueOf(Long.parseLong(trimId))).ifPresent(p -> {
                            String img = productImageRepository
                                    .findByProduct_IdAndThumbnailTrue(p.getId())
                                    .map(ProductImage::getImagePath).orElse("");
                            items.add(Map.of(
                                    "type", "product",
                                    "name", p.getTitle(),
                                    "price", String.format("%,.0f VND", (double) p.getPrice()),
                                    "image", img,
                                    "slug", p.getSlug()
                            ));
                        });
                        case "collection" -> collectionRepository.findById(String.valueOf(Long.parseLong(trimId))).ifPresent(c ->
                                items.add(Map.of(
                                        "type", "collection",
                                        "name", c.getTitle(),
                                        "price", "",
                                        "image", c.getThumbnail() != null ? c.getThumbnail() : "",
                                        "slug", c.getSlug()
                                ))
                        );
                        case "service" -> serviceRepository.findById(String.valueOf(Long.parseLong(trimId))).ifPresent(s ->
                                items.add(Map.of(
                                        "type", "service",
                                        "name", s.getTitle(),
                                        "price", s.getPrice() != null ? s.getPrice().toString() : "",
                                        "image", s.getThumbnail() != null ? s.getThumbnail() : "",
                                        "slug", s.getSlug()
                                ))
                        );
                    }
                } catch (NumberFormatException ignored) {}
            }
        }
        return items;
    }
}