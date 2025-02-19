package com.ecommerce.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidatedOrderRequest {
    private String customerId;
    private List<ValidatedOrderItem> items;
    private BigDecimal totalAmount;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidatedOrderItem {
        private String productId;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal subtotal;
    }
}
