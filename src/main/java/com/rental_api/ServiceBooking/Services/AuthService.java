package com.rental_api.ServiceBooking.Services;


import com.rental_api.ServiceBooking.Dto.Request.RegisterRequest;
import com.rental_api.ServiceBooking.Dto.Response.AuthResponse;

public interface AuthService {

    // Register a new user
    AuthResponse register(RegisterRequest request);

    // Optional for future: login method
    // AuthResponse login(LoginRequest request);
}

