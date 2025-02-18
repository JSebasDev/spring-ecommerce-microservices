package com.ecommerce.customer.service;

import com.ecommerce.customer.dto.request.LoginRequest;
import com.ecommerce.customer.dto.request.UserRequest;
import com.ecommerce.customer.dto.response.AuthResponse;
import com.ecommerce.customer.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(UserRequest request);
    AuthResponse login(LoginRequest request);
    UserResponse validateToken(String token);
}
