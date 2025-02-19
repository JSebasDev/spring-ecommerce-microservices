package com.ecommerce.order.mapper;

import com.ecommerce.order.domain.entity.OrderItem;
import com.ecommerce.order.dto.request.OrderItemRequest;
import com.ecommerce.order.dto.response.OrderItemResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    OrderItem toEntity(OrderItemRequest request);

    OrderItemResponse toResponse(OrderItem orderItem);
}