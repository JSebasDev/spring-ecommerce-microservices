// controller/AuthController.java
package com.ecommerce.customer.controller;

import com.ecommerce.customer.dto.request.LoginRequest;
import com.ecommerce.customer.dto.response.ApiResponse;
import com.ecommerce.customer.dto.response.AuthResponse;
import com.ecommerce.customer.dto.response.UserResponse;
import com.ecommerce.customer.dto.request.UserRequest;
import com.ecommerce.customer.service.AuthService;
import com.ecommerce.customer.util.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody UserRequest request) {
        UserResponse user = authService.register(request);
        ApiResponse<UserResponse> response = ResponseBuilder.build(HttpStatus.CREATED, "User registered successfully", user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);
        ApiResponse<AuthResponse> response = ResponseBuilder.build(HttpStatus.OK, "Login successful", authResponse);

        return ResponseEntity.ok(response);
    }
}
