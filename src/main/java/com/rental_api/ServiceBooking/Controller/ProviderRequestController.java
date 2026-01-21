package com.rental_api.ServiceBooking.Controller;

import com.rental_api.ServiceBooking.Dto.Request.ProviderRequestDto;
import com.rental_api.ServiceBooking.Dto.Response.ApiResponse;
import com.rental_api.ServiceBooking.Entity.ProviderRequest;
import com.rental_api.ServiceBooking.Entity.User;
import com.rental_api.ServiceBooking.Repository.ProviderRequestRepository;
import com.rental_api.ServiceBooking.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/provider-requests")
@RequiredArgsConstructor
public class ProviderRequestController {

    private final ProviderRequestRepository providerRequestRepository;
    private final UserRepository userRepository;

    // -------------------------------
    // User submits a request to become a provider
    // -------------------------------
    @PostMapping("/request")
    public ResponseEntity<ApiResponse<ProviderRequest>> submitRequest(
            @RequestBody ProviderRequestDto dto,
            Authentication authentication
    ) {
        String email = authentication.getName(); // username is email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProviderRequest request = ProviderRequest.builder()
                .user(user)
                .bio(dto.getBio())
                .experience(dto.getExperience())
                .build();

        ProviderRequest saved = providerRequestRepository.save(request);

        ApiResponse<ProviderRequest> response = new ApiResponse<>();
        response.setStatus(200);
        response.setMessage("Request submitted successfully");
        response.setData(saved);

        return ResponseEntity.ok(response);
    }

    // -------------------------------
    // Admin: Get all provider requests
    // -------------------------------
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ProviderRequest>>> getAllRequests() {
        List<ProviderRequest> requests = providerRequestRepository.findAll();

        ApiResponse<List<ProviderRequest>> response = new ApiResponse<>();
        response.setStatus(200);
        response.setMessage("All provider requests fetched successfully");
        response.setData(requests);

        return ResponseEntity.ok(response);
    }

    // -------------------------------
    // Admin: Approve or reject a request
    // -------------------------------
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ProviderRequest>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        ProviderRequest request = providerRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!status.equalsIgnoreCase("APPROVED") &&
            !status.equalsIgnoreCase("REJECTED")) {
            throw new RuntimeException("Invalid status value");
        }

        request.setStatus(status.toUpperCase());
        ProviderRequest updated = providerRequestRepository.save(request);

        ApiResponse<ProviderRequest> response = new ApiResponse<>();
        response.setStatus(200);
        response.setMessage("Request status updated");
        response.setData(updated);

        return ResponseEntity.ok(response);
    }
}
