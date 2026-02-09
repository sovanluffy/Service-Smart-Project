package com.rental_api.ServiceBooking.Services;

import com.rental_api.ServiceBooking.Dto.Response.UserResponse;
import com.rental_api.ServiceBooking.Entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService {

    // ✅ Accept HttpServletRequest to get current user
    User getCurrentUser(HttpServletRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);
    UserResponse getUserByEmail(String email);

    UserResponse updateUser(Long id, UserResponse updatedData);

    void deleteUser(Long id);

    UserResponse getUserEntityByEmail(String email);
}
