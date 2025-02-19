package com.ecommerce.order.client;

import com.ecommerce.order.config.FeignClientConfig;
import com.ecommerce.order.dto.response.UserResponse;
import com.ecommerce.order.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "customer-service",
        url = "${api.gateway.url}",
        configuration = FeignClientConfig.class
)
public interface CustomerFeignClient {

    @GetMapping("/api/v1/users/{id}")
    ApiResponse<UserResponse> getUserById(@PathVariable("id") String id);

    @GetMapping("/api/v1/users/me")
    ApiResponse<UserResponse> getCurrentUser();
}