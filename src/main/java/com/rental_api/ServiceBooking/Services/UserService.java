package com.rental_api.ServiceBooking.Services;

import com.rental_api.ServiceBooking.Dto.Response.UserResponse;

import java.util.List;

public interface UserService {

    // Get all users
    List<UserResponse> getAllUsers();

    // Get user by ID
    UserResponse getUserById(Long id);

    // Update an existing user
    UserResponse updateUser(Long id, UserResponse updatedData);

    // Delete a user
    void deleteUser(Long id);
}
