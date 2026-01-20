package com.rental_api.ServiceBooking.Services.impl;

import com.rental_api.ServiceBooking.Dto.Request.RegisterRequest;
import com.rental_api.ServiceBooking.Dto.Response.AuthResponse;
import com.rental_api.ServiceBooking.Entity.Role;
import com.rental_api.ServiceBooking.Entity.User;
import com.rental_api.ServiceBooking.Entity.UserRole;
import com.rental_api.ServiceBooking.Exception.ConflictException;
import com.rental_api.ServiceBooking.Repository.RoleRepository;
import com.rental_api.ServiceBooking.Repository.UserRepository;
import com.rental_api.ServiceBooking.Repository.UserRoleRepository;
import com.rental_api.ServiceBooking.Security.JwtUtils;
import com.rental_api.ServiceBooking.Services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("Email already exists");
        }

        // Save User
        User user = User.builder()
                .fullname(request.getFullname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .location(request.getLocation())
                .build();
        userRepository.save(user);

        // Ensure CUSTOMER role exists
        Role customerRole = roleRepository.findByName("CUSTOMER")
                .orElseGet(() -> roleRepository.save(
                        Role.builder()
                                .name("CUSTOMER")
                                .description("Default customer role")
                                .build()
                ));

        // Create UserRole entry
        UserRole userRole = UserRole.builder()
                .user(user)
                .role(customerRole)
                .build();
        userRoleRepository.save(userRole);

        // Prepare JWT
        List<String> roles = List.of(customerRole.getName());
        List<Long> roleIds = List.of(customerRole.getId());
        String token = jwtUtils.generateToken(user.getId(), user.getEmail(), user.getEmail(), roles, roleIds);

        return AuthResponse.builder()
                .userId(user.getId())
                .fullname(user.getFullname())
                .email(user.getEmail())
                .token(token)
                .message("Registered successfully")
                .build();
    }
}
