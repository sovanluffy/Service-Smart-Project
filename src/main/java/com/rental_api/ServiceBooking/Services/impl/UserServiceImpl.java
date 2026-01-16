package com.rental_api.ServiceBooking.Services.impl;

import com.rental_api.ServiceBooking.Dtos.Response.UserResponse;
import com.rental_api.ServiceBooking.Entity.User;
import com.rental_api.ServiceBooking.Mapper.UserResponseMapper;
import com.rental_api.ServiceBooking.Repository.UserRepository;
import com.rental_api.ServiceBooking.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserResponseMapper::toResponse)
                .collect(Collectors.toList());
    }
}
