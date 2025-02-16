// util/ResponseBuilder.java
package com.ecommerce.customer.util;

import com.ecommerce.customer.dto.response.ApiResponse;
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

    public static <T> ApiResponse<T> success(String message, T details) {
        return build(HttpStatus.OK, message, details);
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String message, T details) {
        return build(status, message, details);
    }
}