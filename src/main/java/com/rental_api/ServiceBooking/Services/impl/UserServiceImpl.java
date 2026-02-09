package com.rental_api.ServiceBooking.Services.impl;

import com.rental_api.ServiceBooking.Dto.Response.UserResponse;
import com.rental_api.ServiceBooking.Entity.User;
import com.rental_api.ServiceBooking.Exception.PropertyNotFoundException;
import com.rental_api.ServiceBooking.Repository.UserRepository;
import com.rental_api.ServiceBooking.Security.JwtUtils;
import com.rental_api.ServiceBooking.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    // ✅ Properly implemented getCurrentUser using JWT from HttpServletRequest
    @Override
    public User getCurrentUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new PropertyNotFoundException("Authorization header missing or invalid");
        }

        String token = authHeader.substring(7); // remove "Bearer "
        if (!jwtUtils.validateToken(token)) {
            throw new PropertyNotFoundException("Invalid or expired token");
        }

        String email = jwtUtils.extractEmail(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new PropertyNotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFoundException("User not found with id: " + id));
        return mapToUserResponse(user);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::mapToUserResponse)
                .orElseThrow(() -> new PropertyNotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserResponse updatedData) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFoundException("User not found with id: " + id));

        user.setFullname(updatedData.getFullname());
        user.setEmail(updatedData.getEmail());
        user.setPhone(updatedData.getPhone());
        user.setAddress(updatedData.getAddress());
        user.setLocation(updatedData.getLocation());

        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    @Override
    public UserResponse getUserEntityByEmail(String email) {
        return getUserByEmail(email);
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFullname(user.getFullname());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setAddress(user.getAddress());
        response.setLocation(user.getLocation());
        response.setRoles(user.getRoles() != null ? user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet()) : null);
        return response;
    }
}
