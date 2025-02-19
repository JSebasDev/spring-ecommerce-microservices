package com.ecommerce.order.mapper;

import com.ecommerce.order.dto.request.ValidatedOrderRequest;
import com.ecommerce.order.domain.entity.Order;
import com.ecommerce.order.dto.request.OrderRequest;
import com.ecommerce.order.dto.response.OrderResponse;
import com.ecommerce.order.dto.response.ProductValidationResponse;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "CREATED")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Order toEntity(ValidatedOrderRequest request);

    OrderResponse toResponse(Order order);

    @Mapping(target = "items", source = "request")
    @Mapping(target = "totalAmount", ignore = true)
    ValidatedOrderRequest toValidatedRequest(
            OrderRequest request,
            @Context Map<String, ProductValidationResponse> validatedProducts
    );

    @Named("mapValidatedItems")
    default List<ValidatedOrderRequest.ValidatedOrderItem> mapValidatedItems(
            OrderRequest request,
            @Context Map<String, ProductValidationResponse> validatedProducts
    ) {
        return request.getItems().stream()
                .map(item -> {
                    ProductValidationResponse product = validatedProducts.get(item.getProductId());
                    return ValidatedOrderRequest.ValidatedOrderItem.builder()
                            .productId(item.getProductId())
                            .quantity(item.getQuantity())
                            .price(product.getPrice())
                            .subtotal(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                            .build();
                })
                .collect(Collectors.toList());
    }
}