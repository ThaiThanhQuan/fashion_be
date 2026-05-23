package com.example.fashion_db.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecentOrderResponse {
    String id;
    String username;
    Long grandTotal;
    String status;
    LocalDateTime createdAt;
}