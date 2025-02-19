package com.ecommerce.order.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductValidationResponse {
    private String id;
    private boolean active;
    private Integer stock;
    private String name;
    private BigDecimal price;
}
