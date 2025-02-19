package com.ecommerce.order.client;

import com.ecommerce.order.config.FeignClientConfig;
import com.ecommerce.order.dto.request.ProductValidationRequest;
import com.ecommerce.order.dto.response.ProductValidationResponse;
import com.ecommerce.order.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "product-service",
        url = "${api.gateway.url}",
        configuration = FeignClientConfig.class
)
public interface ProductFeignClient {

    @PostMapping("/api/v1/products/validate")
    ApiResponse<List<ProductValidationResponse>> validateProducts(@RequestBody ProductValidationRequest request);
}