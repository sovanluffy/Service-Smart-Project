package com.rental_api.ServiceBooking.Services.impl;

import com.rental_api.ServiceBooking.Dto.Request.LoginRequest;
import com.rental_api.ServiceBooking.Dto.Request.RegisterRequest;
import com.rental_api.ServiceBooking.Dto.Response.AuthResponse;
import com.rental_api.ServiceBooking.Entity.Role;
import com.rental_api.ServiceBooking.Entity.User;
import com.rental_api.ServiceBooking.Exception.ConflictException;
import com.rental_api.ServiceBooking.Repository.RoleRepository;
import com.rental_api.ServiceBooking.Repository.UserRepository;
import com.rental_api.ServiceBooking.Services.AuthService;
import com.rental_api.ServiceBooking.Security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Processing registration for email: {}", request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("Email already exists");
        }

        // 1️⃣ Create User and Hash Password
        User user = User.builder()
                .fullname(request.getFullname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .location(request.getLocation())
                .build();

        // 2️⃣ Assign Default Role (CUSTOMER)
        Role customerRole = roleRepository.findByName("CUSTOMER")
                .orElseGet(() -> {
                    Role nr = new Role();
                    nr.setName("CUSTOMER");
                    nr.setDescription("Default role for customers");
                    return roleRepository.save(nr);
                });

        // Assign role to user
        user.setRoles(Set.of(customerRole));
        user = userRepository.save(user); // save user with role

        // 3️⃣ Prepare roles for JWT and response
        List<String> roleNames = user.getRoles().stream().map(Role::getName).toList();
        List<Long> roleIds = user.getRoles().stream().map(Role::getId).toList();

        // 4️⃣ Generate JWT Token
        String token = jwtUtils.generateToken(user.getId(), user.getEmail(), user.getEmail(), roleNames, roleIds);

        // 5️⃣ Return AuthResponse
        return mapToAuthResponse(user, token, "Registered successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        log.info("Processing login for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        List<String> roleNames = user.getRoles().stream().map(Role::getName).toList();
        List<Long> roleIds = user.getRoles().stream().map(Role::getId).toList();

        String token = jwtUtils.generateToken(user.getId(), user.getEmail(), user.getEmail(), roleNames, roleIds);

        return mapToAuthResponse(user, token, "Login successful");
    }

    // Map User entity to AuthResponse DTO
    private AuthResponse mapToAuthResponse(User user, String token, String message) {
        return AuthResponse.builder()
                .userId(user.getId())
                .fullname(user.getFullname())
                .email(user.getEmail())
                .message(message)
                .token(token)
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .roleIds(user.getRoles().stream().map(Role::getId).toList())
                .build();
    }
}
