package com.rental_api.ServiceBooking.Services;

import com.rental_api.ServiceBooking.Dtos.Response.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllUsers();
}
