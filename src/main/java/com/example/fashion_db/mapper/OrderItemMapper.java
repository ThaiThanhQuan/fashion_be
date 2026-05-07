package com.example.fashion_db.mapper;

import com.example.fashion_db.dto.response.OrderItemResponse;
import com.example.fashion_db.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderItemMapper {
    @Mapping(target = "totalPrice", expression = "java(orderItem.getPrice() * orderItem.getQuantity())")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);
}