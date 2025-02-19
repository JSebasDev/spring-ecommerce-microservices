package com.ecommerce.order.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotNull(message = "Customer ID is required")
    private String customerId;

    @NotEmpty(message = "Order must have at least one item")
    @Size(min = 1, message = "Order must have at least one item")
    @Valid
    private List<OrderItemRequest> items;
}