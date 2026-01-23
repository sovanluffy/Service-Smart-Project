package com.rental_api.ServiceBooking.Controller;

import com.rental_api.ServiceBooking.Dto.Request.ServiceRequest;
import com.rental_api.ServiceBooking.Dto.Response.ServiceResponse;
import com.rental_api.ServiceBooking.Entity.ServiceEntity;
import com.rental_api.ServiceBooking.Security.CustomUserDetails;
import com.rental_api.ServiceBooking.Services.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    @PostMapping("/create")
    public ResponseEntity<ServiceResponse> createService(@RequestBody ServiceRequest request) {
        Long userId = ((CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUserId();

        ServiceEntity service = serviceService.createService(userId, request);
        return ResponseEntity.ok(mapToResponse(service));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ServiceResponse>> getAllServices() {
        List<ServiceEntity> services = serviceService.getAllServices();
        return ResponseEntity.ok(
                services.stream().map(this::mapToResponse).collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable Long id) {
        ServiceEntity service = serviceService.getServiceById(id);
        return ResponseEntity.ok(mapToResponse(service));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> updateService(@PathVariable Long id, @RequestBody ServiceRequest request) {
        Long userId = ((CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUserId();

        ServiceEntity updatedService = serviceService.updateService(id, userId, request);
        return ResponseEntity.ok(mapToResponse(updatedService));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteService(@PathVariable Long id) {
        Long userId = ((CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUserId();

        serviceService.deleteService(id, userId);
        return ResponseEntity.ok("Service deleted successfully");
    }

    private ServiceResponse mapToResponse(ServiceEntity service) {
        return new ServiceResponse(
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getPrice(),
                service.getDuration(),
                service.getCreatedAt(),
                service.getUpdatedAt()
        );
    }
}
