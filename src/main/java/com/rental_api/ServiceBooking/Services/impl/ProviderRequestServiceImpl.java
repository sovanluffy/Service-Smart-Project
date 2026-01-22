package com.rental_api.ServiceBooking.Services.impl;

import com.rental_api.ServiceBooking.Dto.Request.ProviderRequestDto;
import com.rental_api.ServiceBooking.Dto.Response.ProviderRequestResponse;
import com.rental_api.ServiceBooking.Dto.Response.UserInfoResponse;
import com.rental_api.ServiceBooking.Entity.ProviderRequest;
import com.rental_api.ServiceBooking.Entity.ServiceProvider;
import com.rental_api.ServiceBooking.Entity.User;
import com.rental_api.ServiceBooking.Exception.ConflictException;
import com.rental_api.ServiceBooking.Exception.UserNotFoundException;
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
    public ProviderRequestResponse createRequest(ProviderRequestDto dto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (providerRequestRepository.existsByUserIdAndStatus(user.getId(), "PENDING")) {
            throw new ConflictException("You already have a pending request.");
        }

        Double experience = dto.getExperience() != null ? dto.getExperience() : 0.0;

        ProviderRequest request = ProviderRequest.builder()
                .user(user)
                .bio(dto.getBio())
                .experience(experience)
                .status("PENDING")
                .build();

        return mapToResponse(providerRequestRepository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProviderRequestResponse> getAllRequests() {
        return providerRequestRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProviderRequestResponse approveRequest(Long requestId) {
        ProviderRequest request = providerRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if ("APPROVED".equals(request.getStatus())) {
            throw new ConflictException("Request already approved.");
        }

        request.setStatus("APPROVED");
        providerRequestRepository.save(request);

        ServiceProvider provider = new ServiceProvider();
        provider.setUser(request.getUser());
        provider.setBio(request.getBio());
        provider.setExperience(request.getExperience());
        provider.setRating(0.0f);

        serviceProviderRepository.save(provider);

        return mapToResponse(request);
    }

    private ProviderRequestResponse mapToResponse(ProviderRequest request) {
        User user = request.getUser();

        UserInfoResponse userInfo = UserInfoResponse.builder()
                .id(user.getId())
                .fullname(user.getFullname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .location(user.getLocation())
                .build();

        return ProviderRequestResponse.builder()
                .id(request.getId())
                .user(userInfo)
                .bio(request.getBio())
                .experience(request.getExperience() != null ? String.valueOf(request.getExperience()) : "0.0")
                .status(request.getStatus())
                .build();
    }
}
