package com.rental_api.ServiceBooking.Controller;

import com.rental_api.ServiceBooking.Dtos.Response.UserResponse;
import com.rental_api.ServiceBooking.Services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User management APIs")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Returns all users with their roles"
    )
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }
}
