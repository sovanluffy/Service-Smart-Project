package com.rental_api.ServiceBooking.Services.impl;

import com.rental_api.ServiceBooking.Dto.Request.LoginRequest;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Processing registration for email: {}", request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("Email already exists");
        }

        // 1. Create User and Hash Password
        User user = new User();
        user.setFullname(request.getFullname());
        user.setEmail(request.getEmail());
        // This uses the BCryptPasswordEncoder from your SecurityConfig
        user.setPassword(passwordEncoder.encode(request.getPassword())); 
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setLocation(request.getLocation());
        user = userRepository.save(user);

        // 2. Assign Default Role (CUSTOMER)
        Role customerRole = roleRepository.findByName("CUSTOMER")
                .orElseGet(() -> {
                    Role nr = new Role();
                    nr.setName("CUSTOMER");
                    nr.setDescription("Default role for customers");
                    return roleRepository.save(nr);
                });

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(customerRole);
        userRoleRepository.save(userRole);

        // 3. Generate JWT Token
        List<String> roles = List.of(customerRole.getName());
        List<Long> roleIds = List.of(customerRole.getId());
        String token = jwtUtils.generateToken(user.getId(), user.getEmail(), user.getEmail(), roles, roleIds);

        return mapToAuthResponse(user, token, "Registered successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        log.info("Processing login for email: {}", request.getEmail());

        // 1. Find User
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        // 2. Check Password (Matches plain text with hashed)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        // 3. Fetch User Roles
        List<UserRole> userRoles = userRoleRepository.findByUser(user);
        List<String> roleNames = userRoles.stream().map(ur -> ur.getRole().getName()).toList();
        List<Long> roleIds = userRoles.stream().map(ur -> ur.getRole().getId()).toList();

        // 4. Generate Token
        String token = jwtUtils.generateToken(user.getId(), user.getEmail(), user.getEmail(), roleNames, roleIds);

        return mapToAuthResponse(user, token, "Login successful");
    }

    // Manual mapping (No Builder used)
    private AuthResponse mapToAuthResponse(User user, String token, String message) {
        AuthResponse response = new AuthResponse();
        response.setUserId(user.getId());
        response.setFullname(user.getFullname());
        response.setEmail(user.getEmail());
        response.setToken(token);
        response.setMessage(message);
        return response;
    }
}