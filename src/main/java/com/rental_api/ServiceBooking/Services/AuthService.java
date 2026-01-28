package com.rental_api.ServiceBooking.Services;

import com.rental_api.ServiceBooking.Dto.Request.LoginRequest; // You'll need to create this
import com.rental_api.ServiceBooking.Dto.Request.RegisterRequest;
import com.rental_api.ServiceBooking.Dto.Response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
        AuthResponse registerAdmin(RegisterRequest request);
        void logout(String token);
}