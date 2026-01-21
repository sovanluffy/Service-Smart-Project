package com.rental_api.ServiceBooking.Controller;

import com.rental_api.ServiceBooking.Dto.Request.LoginRequest;
import com.rental_api.ServiceBooking.Dto.Request.RegisterRequest;
import com.rental_api.ServiceBooking.Dto.Response.ApiResponse;
import com.rental_api.ServiceBooking.Dto.Response.AuthResponse;
import com.rental_api.ServiceBooking.Services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication APIs")
public class AuthController {

    private final AuthService authService;

    // -------------------------------
    // Register CUSTOMER
    // -------------------------------
    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Creates a new user and returns JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse authResponse = authService.register(request);

        ApiResponse<AuthResponse> response = new ApiResponse<>();
        response.setStatus(200);
        response.setMessage("Registered successfully");
        response.setData(authResponse);

        return ResponseEntity.ok(response);
    }

    // -------------------------------
    // Register ADMIN
    // -------------------------------
    @PostMapping("/register-admin")
    @Operation(summary = "Register new admin", description = "Creates a new ADMIN user and returns JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> registerAdmin(@Valid @RequestBody RegisterRequest request) {
        AuthResponse authResponse = authService.registerAdmin(request);

        ApiResponse<AuthResponse> response = new ApiResponse<>();
        response.setStatus(200);
        response.setMessage("Admin registered successfully");
        response.setData(authResponse);

        return ResponseEntity.ok(response);
    }

    // -------------------------------
    // Login
    // -------------------------------
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticates user and returns JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);

        ApiResponse<AuthResponse> response = new ApiResponse<>();
        response.setStatus(200);
        response.setMessage("Login successful");
        response.setData(authResponse);

        return ResponseEntity.ok(response);
    }

    // -------------------------------
    // Test Endpoint
    // -------------------------------
    @GetMapping("/hello")
    @Operation(summary = "Test endpoint", description = "Simple test endpoint to verify API is running")
    public ResponseEntity<ApiResponse<String>> hello() {
        ApiResponse<String> response = new ApiResponse<>();
        response.setStatus(200);
        response.setMessage("Hello endpoint works!");
        response.setData("Hello World");

        return ResponseEntity.ok(response);
    }
}
