package com.example.fashion_db.mapper;

import com.example.fashion_db.dto.response.OrderResponse;
import com.example.fashion_db.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, AddressMapper.class})
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "addressId", source = "address.id")
    OrderResponse toOrderResponse(Order order);
}
