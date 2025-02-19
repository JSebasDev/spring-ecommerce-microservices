package com.ecommerce.product.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductValidationResponse {
    private String id;
    private boolean active;
    private Integer stock;
}
