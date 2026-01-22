package com.rental_api.ServiceBooking.Controller;

import com.rental_api.ServiceBooking.Dto.Request.ProviderRequestDto;
import com.rental_api.ServiceBooking.Dto.Response.BaseResponse;
import com.rental_api.ServiceBooking.Dto.Response.ProviderRequestResponse;
import com.rental_api.ServiceBooking.Services.ProviderRequestService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/provider-requests")
@RequiredArgsConstructor
public class ProviderRequestController {

    private final ProviderRequestService providerRequestService;

    // CREATE PROVIDER REQUEST
    @PostMapping
    public ResponseEntity<BaseResponse<ProviderRequestResponse>> createRequest(
            @RequestBody ProviderRequestDto dto,
            Authentication authentication
    ) {
        String email = authentication.getName();
        ProviderRequestResponse response =
                providerRequestService.createRequest(dto, email);

        return ResponseEntity.ok(
                BaseResponse.success(response, "Provider request created successfully")
        );
    }

    // GET ALL PROVIDER REQUESTS (ADMIN ONLY)
    @GetMapping
    public ResponseEntity<BaseResponse<List<ProviderRequestResponse>>> getAllRequests() {
        List<ProviderRequestResponse> requests =
                providerRequestService.getAllRequests();

        return ResponseEntity.ok(
                BaseResponse.success(requests, "All provider requests retrieved successfully")
        );
    }

    // APPROVE REQUEST (ADMIN ONLY)
    @PostMapping("/approve/{requestId}")
    public ResponseEntity<BaseResponse<ProviderRequestResponse>> approveRequest(
            @PathVariable Long requestId
    ) {
        ProviderRequestResponse response =
                providerRequestService.approveRequest(requestId);

        return ResponseEntity.ok(
                BaseResponse.success(response, "Congratulations! You are now a provider")
        );
    }

    // REJECT REQUEST (ADMIN ONLY)
    @PostMapping("/reject/{requestId}")
    public ResponseEntity<BaseResponse<ProviderRequestResponse>> rejectRequest(
            @PathVariable Long requestId
    ) {
        ProviderRequestResponse response =
                providerRequestService.rejectRequest(requestId);

        return ResponseEntity.ok(
                BaseResponse.success(response, "You can't be a provider now")
        );
    }
}
