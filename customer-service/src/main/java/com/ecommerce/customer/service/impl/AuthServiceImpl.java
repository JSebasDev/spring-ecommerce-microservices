package com.ecommerce.customer.service.impl;

import com.ecommerce.customer.domain.entity.User;
import com.ecommerce.customer.domain.entity.UserPrincipal;
import com.ecommerce.customer.domain.enums.Role;
import com.ecommerce.customer.dto.request.LoginRequest;
import com.ecommerce.customer.dto.request.UserRequest;
import com.ecommerce.customer.dto.response.AuthResponse;
import com.ecommerce.customer.dto.response.UserResponse;
import com.ecommerce.customer.exception.DuplicateEntityException;
import com.ecommerce.customer.exception.UnauthorizedException;
import com.ecommerce.customer.mapper.UserMapper;
import com.ecommerce.customer.repository.UserRepository;
import com.ecommerce.customer.security.JwtTokenProvider;
import com.ecommerce.customer.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse register(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEntityException("Email already registered");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());

        user = userRepository.save(user);

        return userMapper.toResponseDto(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        UserPrincipal userPrincipal = new UserPrincipal(user);
        String token = jwtTokenProvider.generateToken(userPrincipal);

        return AuthResponse.builder()
                .username(user.getEmail())
                .token(token)
                .build();
    }

    @Override
    public UserResponse validateToken(String token) {
        if (token == null) {
            throw new UnauthorizedException("Token is required");
        }

        try {
            String email = jwtTokenProvider.extractUsername(token);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UnauthorizedException("User not found"));

            UserPrincipal userPrincipal = new UserPrincipal(user);

            if (!jwtTokenProvider.isTokenValid(token, userPrincipal)) {
                throw new UnauthorizedException("Invalid token");
            }

            return userMapper.toResponseDto(user);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid token");
        }
    }
}
