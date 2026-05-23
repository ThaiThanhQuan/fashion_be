package com.example.fashion_db.dto.response;

import com.example.fashion_db.enums.OrderStatus;
import com.example.fashion_db.enums.PaymentMethod;
import com.example.fashion_db.enums.PaymentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    String id;
    String userId;
    String addressId;
    AddressResponse address;
    Long subtotal;
    Long shippingFee;
    Long tax;
    Long grandTotal;
    OrderStatus status;
    PaymentMethod paymentMethod;
    PaymentStatus paymentStatus;
    LocalDateTime createdAt;
    List<OrderItemResponse> orderItems;
    String paymentUrl;
}
