package com.rental_api.ServiceBooking.Controller;

import com.rental_api.ServiceBooking.Dto.Request.ProviderRequestDto;
import com.rental_api.ServiceBooking.Dto.Response.ApiResponse;
import com.rental_api.ServiceBooking.Dto.Response.ProviderRequestResponse;
import com.rental_api.ServiceBooking.Services.ProviderRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/provider-requests")
@RequiredArgsConstructor
public class ProviderRequestController {

    private final ProviderRequestService providerRequestService;

    @Operation(
            summary = "Create a new provider request",
            description = "Creates a provider request for the logged-in user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Provider request created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict: pending request exists",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
                    )
            }
    )
    @PostMapping
    public ResponseEntity<ApiResponse<ProviderRequestResponse>> createRequest(
            @RequestBody ProviderRequestDto dto,
            Authentication authentication
    ) {
        String email = authentication.getName();
        ProviderRequestResponse response = providerRequestService.createRequest(dto, email);
        return ResponseEntity.ok(ApiResponse.success(response, "Provider request created successfully"));
    }

    @Operation(
            summary = "Get all provider requests",
            description = "Retrieve all provider requests (Admin only)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List retrieved successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProviderRequestResponse>>> getAllRequests() {
        List<ProviderRequestResponse> requests = providerRequestService.getAllRequests();
        return ResponseEntity.ok(ApiResponse.success(requests, "All provider requests retrieved successfully"));
    }

    @Operation(
            summary = "Approve a provider request",
            description = "Approve a provider request by its ID (Admin only)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Provider request approved successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Request not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict: request already approved",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
                    )
            }
    )
    @PostMapping("/approve/{requestId}")
    public ResponseEntity<ApiResponse<ProviderRequestResponse>> approveRequest(
            @PathVariable Long requestId
    ) {
        ProviderRequestResponse response = providerRequestService.approveRequest(requestId);
        return ResponseEntity.ok(ApiResponse.success(response, "Provider request approved successfully"));
    }
}
