package com.example.fashion_db.enums;

public enum OrderStatus {
    PENDING,      // chờ xác nhận
    CONFIRMED,    // đã xác nhận
    SHIPPING,     // đang giao
    DELIVERED,    // đã giao
    CANCELLED,    // đã hủy
    REFUNDED      // đã hoàn tiền
}
