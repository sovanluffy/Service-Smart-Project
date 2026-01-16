package com.rental_api.ServiceBooking.Services.impl;

import com.rental_api.ServiceBooking.Dto.Request.RegisterRequest;
import com.rental_api.ServiceBooking.Dto.Response.AuthResponse;
import com.rental_api.ServiceBooking.Entity.User;
import com.rental_api.ServiceBooking.Repository.UserRepository;
import com.rental_api.ServiceBooking.Services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(RegisterRequest request) {

        // Check if email exists
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Create user entity
        User user = User.builder()
                .fullname(request.getFullname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .location(request.getLocation())
                .build();

        userRepository.save(user);

        // Return response DTO
        return AuthResponse.builder()
                .userId(user.getId())
                .fullname(user.getFullname())
                .email(user.getEmail())
                .message("Registered successfully")
                .build();
    }
}
