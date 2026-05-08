package com.example.fashion_db.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkflowResponse {
    String id;
    String no;
    String title;
    String content;
    String serviceId;
}