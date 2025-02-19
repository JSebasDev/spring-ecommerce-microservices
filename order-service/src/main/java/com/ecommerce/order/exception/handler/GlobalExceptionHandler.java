package com.ecommerce.order.exception.handler;

import com.ecommerce.order.dto.response.ApiResponse;
import com.ecommerce.order.exception.ErrorDetails;
import com.ecommerce.order.exception.KafkaProducerException;
import com.ecommerce.order.exception.OrderNotFoundException;
import com.ecommerce.order.exception.OrderValidationException;
import com.ecommerce.order.util.ResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<ErrorDetails> handleOrderNotFoundException(
            OrderNotFoundException ex,
            HttpServletRequest request) {

        ErrorDetails errorDetails = buildErrorDetails(ex.getMessage(), request.getRequestURI(), null);
        log.error("Order not found: {}", ex.getMessage());

        return ResponseBuilder.build(
                HttpStatus.NOT_FOUND,
                "Order not found",
                errorDetails
        );
    }

    @ExceptionHandler(OrderValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<ErrorDetails> handleOrderValidationException(
            OrderValidationException ex,
            HttpServletRequest request) {

        ErrorDetails errorDetails = buildErrorDetails(ex.getMessage(), request.getRequestURI(), null);
        log.error("Validation error: {}", ex.getMessage());

        return ResponseBuilder.build(
                HttpStatus.BAD_REQUEST,
                "Validation error",
                errorDetails
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<ErrorDetails> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorDetails errorDetails = buildErrorDetails(
                "Validation failed",
                request.getRequestURI(),
                errors
        );

        log.error("Validation errors: {}", errors);

        return ResponseBuilder.build(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                errorDetails
        );
    }

    @ExceptionHandler(KafkaProducerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<ErrorDetails> handleKafkaProducerException(
            KafkaProducerException ex,
            HttpServletRequest request) {

        ErrorDetails errorDetails = buildErrorDetails(ex.getMessage(), request.getRequestURI(), null);
        log.error("Kafka producer error: {}", ex.getMessage());

        return ResponseBuilder.build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error processing order",
                errorDetails
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<ErrorDetails> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        ErrorDetails errorDetails = buildErrorDetails(
                "Invalid request body",
                request.getRequestURI(),
                null
        );

        log.error("Invalid request body: {}", ex.getMessage());

        return ResponseBuilder.build(
                HttpStatus.BAD_REQUEST,
                "Invalid request body",
                errorDetails
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<ErrorDetails> handleAllUncaughtException(
            Exception ex,
            HttpServletRequest request) {

        ErrorDetails errorDetails = buildErrorDetails(
                "An unexpected error occurred",
                request.getRequestURI(),
                null
        );

        log.error("Unexpected error: ", ex);

        return ResponseBuilder.build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                errorDetails
        );
    }

    private ErrorDetails buildErrorDetails(String message, String path, Map<String, String> errors) {
        return ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .path(path)
                .errors(errors)
                .build();
    }
}