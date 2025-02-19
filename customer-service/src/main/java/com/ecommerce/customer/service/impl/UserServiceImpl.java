package com.ecommerce.customer.service.impl;

import com.ecommerce.customer.domain.entity.User;
import com.ecommerce.customer.repository.UserRepository;
import com.ecommerce.customer.dto.request.UserRequest;
import com.ecommerce.customer.dto.response.UserResponse;
import com.ecommerce.customer.exception.UnauthorizedException;
import com.ecommerce.customer.exception.NotFoundException;
import com.ecommerce.customer.mapper.UserMapper;
import com.ecommerce.customer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse getCurrentUser() {
        User user = getCurrentUserEntity();
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        User currentUser = getCurrentUserEntity();

        if (!currentUser.getId().equals(id)) {
            throw new UnauthorizedException("You can only update your own profile");
        }

        currentUser.setFirstName(request.getFirstName());
        currentUser.setLastName(request.getLastName());
        currentUser.setEmail(request.getEmail());

        User updatedUser = userRepository.save(currentUser);
        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User currentUser = getCurrentUserEntity();

        if (!currentUser.getId().equals(id)) {
            throw new UnauthorizedException("You can only delete your own profile");
        }

        userRepository.deleteById(id);
    }

    private User getCurrentUserEntity() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }
}
