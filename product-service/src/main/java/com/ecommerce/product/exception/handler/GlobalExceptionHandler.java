package com.ecommerce.product.exception.handler;

import com.ecommerce.product.dto.response.ApiResponse;
import com.ecommerce.product.exception.CustomException;
import com.ecommerce.product.util.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity handleCustomException(CustomException ex) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", ex.getMessage());

        ApiResponse response = ResponseBuilder.build(
                ex.getStatus(), ex.getMessage(), errorDetails);

        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errorDetails = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((FieldError error) ->
                errorDetails.put(error.getField(), error.getDefaultMessage()));

        ApiResponse response = ResponseBuilder.build(
                HttpStatus.BAD_REQUEST, "Validation failed", errorDetails);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleGlobalException(Exception ex) {
        log.error("Unhandled exception occurred: ", ex);
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", ex.getMessage());

        ApiResponse response = ResponseBuilder.build(
                HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", errorDetails);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
