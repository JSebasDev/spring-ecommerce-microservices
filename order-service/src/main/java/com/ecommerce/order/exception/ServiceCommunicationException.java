package com.ecommerce.order.exception;

public class ServiceCommunicationException extends RuntimeException {
    public ServiceCommunicationException(String message) {
        super(message);
    }
}