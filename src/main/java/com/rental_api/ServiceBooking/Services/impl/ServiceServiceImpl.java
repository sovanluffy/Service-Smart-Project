package com.rental_api.ServiceBooking.Services.impl;

import com.rental_api.ServiceBooking.Dto.Request.ServiceRequest;
import com.rental_api.ServiceBooking.Dto.Response.ServiceResponse;
import com.rental_api.ServiceBooking.Entity.ServiceEntity;
import com.rental_api.ServiceBooking.Entity.ServiceProvider;
import com.rental_api.ServiceBooking.Repository.ServiceRepository;
import com.rental_api.ServiceBooking.Repository.ServiceProviderRepository;
import com.rental_api.ServiceBooking.Services.ServiceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceProviderRepository providerRepository;

    public ServiceServiceImpl(ServiceRepository serviceRepository,
                              ServiceProviderRepository providerRepository) {
        this.serviceRepository = serviceRepository;
        this.providerRepository = providerRepository;
    }

    @Override
    public ServiceResponse createService(Long userId, ServiceRequest request) {
        // 1️⃣ Find provider by userId
        ServiceProvider provider = providerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Provider profile not found"));

        // 2️⃣ Create new service entity
        ServiceEntity service = new ServiceEntity();
        service.setProvider(provider);
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setDuration(request.getDuration());
        service.setCreatedAt(LocalDateTime.now());
        service.setUpdatedAt(LocalDateTime.now());

        // 3️⃣ Save to DB
        ServiceEntity savedService = serviceRepository.save(service);

        // 4️⃣ Map to DTO
        return ServiceResponse.builder()
                .id(savedService.getId())
                .name(savedService.getName())
                .description(savedService.getDescription())
                .price(savedService.getPrice())
                .duration(savedService.getDuration())
                .providerName(provider.getUser().getFullname()) // ✅ use getFullname()
                .createdAt(savedService.getCreatedAt())
                .updatedAt(savedService.getUpdatedAt())
                .build();
    }
}
