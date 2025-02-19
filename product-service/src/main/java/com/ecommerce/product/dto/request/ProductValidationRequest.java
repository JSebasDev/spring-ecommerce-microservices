package com.ecommerce.product.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class ProductValidationRequest {
    private List<String> productIds;
}
