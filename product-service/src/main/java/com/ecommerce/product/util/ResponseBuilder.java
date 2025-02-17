package com.ecommerce.product.util;

import com.ecommerce.product.dto.response.ApiResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseBuilder {

    public static <T> ApiResponse<T> build(HttpStatus status, String message, T details) {
        return ApiResponse.<T>builder()
                .status(status.value())
                .message(message)
                .details(details)
                .build();
    }
}
