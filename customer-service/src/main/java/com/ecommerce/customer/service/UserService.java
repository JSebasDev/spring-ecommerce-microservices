package com.ecommerce.customer.service;

import com.ecommerce.customer.dto.request.UserRequest;
import com.ecommerce.customer.dto.response.UserResponse;

public interface UserService {
    UserResponse getCurrentUser();
    UserResponse getUserById(Long id);
    UserResponse updateUser(Long id, UserRequest request);
    void deleteUser(Long id);
}
