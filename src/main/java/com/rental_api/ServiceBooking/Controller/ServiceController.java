package com.rental_api.ServiceBooking.Controller;

import com.rental_api.ServiceBooking.Dto.Request.ServiceRequest;
import com.rental_api.ServiceBooking.Dto.Response.ServiceResponse;
import com.rental_api.ServiceBooking.Services.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    /**
     * Create a new service for a provider
     * @param userId  the ID of the provider's user
     * @param request the service request payload
     * @return the created service
     */
    @PostMapping("/{userId}")
    public ResponseEntity<ServiceResponse> createService(
            @PathVariable Long userId,
            @RequestBody ServiceRequest request
    ) {
        ServiceResponse response = serviceService.createService(userId, request);
        return ResponseEntity.ok(response);
    }
}
