package com.ecommerce.order.exception;

public class ProductValidationException extends RuntimeException {
    public ProductValidationException(String message) {
        super(message);
    }
}
