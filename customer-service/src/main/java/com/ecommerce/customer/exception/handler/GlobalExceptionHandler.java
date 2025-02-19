package com.ecommerce.customer.exception.handler;

import com.ecommerce.customer.dto.response.ErrorResponse;
import com.ecommerce.customer.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        log.error("Custom exception occurred: {}", ex.getMessage());
        return createErrorResponse(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        log.error("Validation error occurred: {}", errors);
        return createErrorResponse("Validation failed", errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        log.error("Unexpected error occurred: ", ex);
        return createErrorResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(String message, HttpStatus status) {
        return createErrorResponse(message, new ArrayList<>(), status);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(String message, List<String> errors, HttpStatus status) {
        ErrorResponse response = ErrorResponse.builder()
                .status(ErrorResponse.Status.builder()
                        .code(status.value())
                        .message(message)
                        .build())
                .errors(errors)
                .metadata(ErrorResponse.Metadata.builder()
                        .timestamp(LocalDateTime.now())
                        .build())
                .build();
        return new ResponseEntity<>(response, status);
    }
}
