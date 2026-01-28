package com.rental_api.ServiceBooking.Controller;

import com.rental_api.ServiceBooking.Dto.Request.LoginRequest;
import com.rental_api.ServiceBooking.Dto.Request.RegisterRequest;
import com.rental_api.ServiceBooking.Dto.Response.ApiResponse;
import com.rental_api.ServiceBooking.Dto.Response.AuthResponse;
import com.rental_api.ServiceBooking.Services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a customer", description = "Registers a new customer account")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody RegisterRequest request) {
        AuthResponse authResponse = authService.register(request);

        ApiResponse<AuthResponse> response = new ApiResponse<>();
        response.setStatus(200);
        response.setMessage("Customer registered successfully");
        response.setData(authResponse);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register-admin")
    @Operation(summary = "Register an admin", description = "Registers a new admin account")
    public ResponseEntity<ApiResponse<AuthResponse>> registerAdmin(@RequestBody RegisterRequest request) {
        AuthResponse authResponse = authService.registerAdmin(request);

        ApiResponse<AuthResponse> response = new ApiResponse<>();
        response.setStatus(200);
        response.setMessage("Admin registered successfully");
        response.setData(authResponse);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate user and return JWT")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);

        ApiResponse<AuthResponse> response = new ApiResponse<>();
        response.setStatus(200);
        response.setMessage("Login successful");
        response.setData(authResponse);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Logs out the current user")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        authService.logout(token);

        ApiResponse<Void> response = new ApiResponse<>();
        response.setStatus(200);
        response.setMessage("Logged out successfully");

        return ResponseEntity.ok(response);
    }
}
