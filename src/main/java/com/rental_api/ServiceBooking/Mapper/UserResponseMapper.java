package com.rental_api.ServiceBooking.Mapper;

import com.rental_api.ServiceBooking.Dtos.Response.RoleResponse;
import com.rental_api.ServiceBooking.Dtos.Response.UserResponse;
import com.rental_api.ServiceBooking.Entity.User;

import java.util.stream.Collectors;

public class UserResponseMapper {

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullname(user.getFullname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .location(user.getLocation())
                .roles(user.getUserRoles() // many-to-many relationship
                        .stream()
                        .map(ur -> RoleResponse.builder()
                                .id(ur.getRole().getId())
                                .name(ur.getRole().getName())
                                .description(ur.getRole().getDescription())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
