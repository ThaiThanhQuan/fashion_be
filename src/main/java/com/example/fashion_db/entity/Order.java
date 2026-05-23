package com.example.fashion_db.entity;

import com.example.fashion_db.enums.OrderStatus;
import com.example.fashion_db.enums.PaymentMethod;
import com.example.fashion_db.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    Long subtotal;
    Long shippingFee;
    Long tax;
    Long grandTotal;

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus;

    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "address_id")
    Address address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Builder.Default
    List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "payment_url", length = 2000)
    String paymentUrl;
}
