package com.rental_api.ServiceBooking.Services;

import com.rental_api.ServiceBooking.Dto.Response.UserResponse;

import java.util.List;

public interface UserService {

    // Get all users
    List<UserResponse> getAllUsers();

    // Get user by ID
    UserResponse getUserById(Long id);
}
