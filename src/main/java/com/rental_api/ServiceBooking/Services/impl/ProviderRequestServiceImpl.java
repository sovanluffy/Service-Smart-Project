package com.rental_api.ServiceBooking.Services.impl;

import com.rental_api.ServiceBooking.Dto.Request.ProviderRequestDto;
import com.rental_api.ServiceBooking.Dto.Response.ProviderRequestResponse;
import com.rental_api.ServiceBooking.Entity.ProviderRequest;
import com.rental_api.ServiceBooking.Entity.ServiceProvider;
import com.rental_api.ServiceBooking.Entity.User;
import com.rental_api.ServiceBooking.Repository.ProviderRequestRepository;
import com.rental_api.ServiceBooking.Repository.ServiceProviderRepository;
import com.rental_api.ServiceBooking.Repository.UserRepository;
import com.rental_api.ServiceBooking.Services.ProviderRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProviderRequestServiceImpl implements ProviderRequestService {

    private final ProviderRequestRepository providerRequestRepository;
    private final UserRepository userRepository;
    private final ServiceProviderRepository serviceProviderRepository;

    @Override
    @Transactional
    public ProviderRequestResponse createRequest(ProviderRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProviderRequest request = new ProviderRequest();
        request.setUser(user);
        request.setBio(dto.getBio());
        request.setExperience(dto.getExperience());
        request.setStatus("PENDING");

        ProviderRequest saved = providerRequestRepository.save(request);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProviderRequestResponse> getAllRequests() {
        return providerRequestRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProviderRequestResponse approveRequest(Long requestId) {
        ProviderRequest request = providerRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus("APPROVED");
        providerRequestRepository.save(request);

        // Create ServiceProvider entry
        ServiceProvider provider = new ServiceProvider();
        provider.setUser(request.getUser());
        provider.setBio(request.getBio());
        provider.setExperience(request.getExperience());
        provider.setRating(0.0f);
        serviceProviderRepository.save(provider);

        return mapToResponse(request);
    }

    private ProviderRequestResponse mapToResponse(ProviderRequest request) {
        ProviderRequestResponse response = new ProviderRequestResponse();
        response.setId(request.getId());
        response.setUserId(request.getUser().getId());
        response.setFullname(request.getUser().getFullname());
        response.setEmail(request.getUser().getEmail());
        response.setBio(request.getBio());
        response.setExperience(request.getExperience());
        response.setStatus(request.getStatus());
        return response;
    }
}
