package com.ecommerce.order.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private String path;
    private Map<String, String> errors;
}