package com.example.fashion_db.dto.request;

import com.example.fashion_db.enums.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    String addressId;
    PaymentMethod paymentMethod;
    List<OrderItemRequest> orderItems;
}