package com.rental_api.ServiceBooking.Services.impl;

import com.rental_api.ServiceBooking.Dto.Request.ServiceRequest;
import com.rental_api.ServiceBooking.Dto.Response.ServiceResponse;
import com.rental_api.ServiceBooking.Entity.ServiceEntity;
import com.rental_api.ServiceBooking.Entity.ServiceProvider;
import com.rental_api.ServiceBooking.Repository.ServiceProviderRepository;
import com.rental_api.ServiceBooking.Repository.ServiceRepository;
import com.rental_api.ServiceBooking.Services.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceProviderRepository providerRepository;

    // CREATE
    @Override
    public ServiceResponse createService(ServiceRequest request) {

        // TEMP FIX (until Spring Security)
        ServiceProvider provider = providerRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Provider not found"));

        ServiceEntity service = ServiceEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .duration(request.getDuration())
                .provider(provider) // 🔥 REQUIRED
                .build();

        serviceRepository.save(service);
        return mapToResponse(service);
    }

    // READ ALL
    @Override
    public List<ServiceResponse> getAllServices() {
        return serviceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // READ BY ID
    @Override
    public ServiceResponse getServiceById(Long id) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        return mapToResponse(service);
    }

    // UPDATE
    @Override
    public ServiceResponse updateService(Long id, ServiceRequest request) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setDuration(request.getDuration());

        serviceRepository.save(service);
        return mapToResponse(service);
    }

    // DELETE
    @Override
    public void deleteService(Long id) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        serviceRepository.delete(service);
    }

    // DTO Mapper
    private ServiceResponse mapToResponse(ServiceEntity service) {
        return ServiceResponse.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .price(service.getPrice())
                .duration(service.getDuration())
                .build();
    }
}
