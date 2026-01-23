package com.rental_api.ServiceBooking.Services.impl;

import com.rental_api.ServiceBooking.Dto.Request.ServiceRequest;
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
    public ServiceEntity createService(Long userId, ServiceRequest request) {
        // Step 1: Find the provider profile
        ServiceProvider provider = providerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Provider profile not found"));

        // Step 2: Create new service
        ServiceEntity service = new ServiceEntity();
        service.setProvider(provider);
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setDuration(request.getDuration());
        service.setCreatedAt(LocalDateTime.now());
        service.setUpdatedAt(LocalDateTime.now());

        // Step 3: Save and return
        return serviceRepository.save(service);
    }
}
