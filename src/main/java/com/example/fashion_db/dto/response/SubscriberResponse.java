package com.example.fashion_db.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriberResponse {
    String id;
    String email;
    LocalDateTime subscribedAt;
}
