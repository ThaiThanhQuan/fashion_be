package com.example.fashion_db.controller;

import com.example.fashion_db.dto.response.ApiResponse;
import com.example.fashion_db.entity.Product;
import com.example.fashion_db.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AIController {

    private final ProductRepository productRepository;

    @Value("${groq.api-key}")
    private String groqApiKey;

    @PostMapping("/chat")
    public ApiResponse<String> chat(
            @RequestBody Map<String, String> request
    ) {

        try {

            String userMessage = request.get("message");

            List<Product> products =
                    productRepository
                            .findTop5ByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                                    userMessage,
                                    userMessage
                            );

            StringBuilder context = new StringBuilder();

            for (Product p : products) {

                context.append(
                        """
                        - %s
                          Giá: %,.0f VND
                          
                        """
                                .formatted(
                                        p.getTitle(),
                                        (double) p.getPrice()
                                )
                );
            }

            String prompt =
                    """
                    Bạn là trợ lý thời trang của COUTURE.

                    Khách hỏi:
                    %s

                    Sản phẩm liên quan:
                    %s

                    Hãy tư vấn ngắn gọn bằng tiếng Việt.
                    """
                            .formatted(userMessage, context);

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            headers.setBearerAuth(groqApiKey);

            Map<String, Object> body = Map.of(
                    "model", "llama-3.3-70b-versatile",
                    "messages", List.of(
                            Map.of(
                                    "role", "user",
                                    "content", prompt
                            )
                    )
            );

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(body, headers);

            ResponseEntity<Map> responseEntity =
                    restTemplate.exchange(
                            "https://api.groq.com/openai/v1/chat/completions",
                            HttpMethod.POST,
                            entity,
                            Map.class
                    );

            Map response = responseEntity.getBody();

            List choices =
                    (List) response.get("choices");

            Map choice =
                    (Map) choices.get(0);

            Map message =
                    (Map) choice.get("message");

            String reply =
                    message.get("content").toString();

            return ApiResponse.<String>builder()
                    .result(reply)
                    .build();

        } catch (Exception e) {

            return ApiResponse.<String>builder()
                    .code(9999)
                    .message(e.getMessage())
                    .build();
        }
    }
}