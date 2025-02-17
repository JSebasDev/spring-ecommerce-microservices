package com.ecommerce.product.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEntityException extends CustomException {
    public DuplicateEntityException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
