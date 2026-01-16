package com.rental_api.ServiceBooking.Dtos.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User response object")
public class UserResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "John Doe")
    private String fullname;

    @Schema(example = "john@gmail.com")
    private String email;

    @Schema(example = "+85512345678")
    private String phone;

    @Schema(example = "Phnom Penh")
    private String address;

    @Schema(example = "Cambodia")
    private String location;

    @Schema(description = "List of user roles")
    private List<RoleResponse> roles;
}
