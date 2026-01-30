package com.rental_api.ServiceBooking.Services;

import com.rental_api.ServiceBooking.Dto.Response.UserResponse;
import com.rental_api.ServiceBooking.Entity.User;

import java.util.List;

public interface UserService {

    static User getCurrentUser() {
        return null;
    }

    // Get all users
    List<UserResponse> getAllUsers();

    // Get user by ID
    UserResponse getUserById(Long id);
    UserResponse getUserByEmail(String email);

    // Update an existing user
    UserResponse updateUser(Long id, UserResponse updatedData);

    // Delete a user
    void deleteUser(Long id);

    UserResponse getUserEntityByEmail(String email);
}
