package com.rental_api.ServiceBooking.Dto.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @Schema(description = "Full name of the user", example = "Sok Dara", required = true)
    private String fullname;

    @Schema(description = "Email address", example = "dara@gmail.com", required = true)
    private String email;

    @Schema(description = "Password", example = "123456", required = true)
    private String password;

    @Schema(description = "Phone number", example = "012345678")
    private String phone;

    @Schema(description = "Address", example = "Phnom Penh")
    private String address;

    @Schema(description = "Location", example = "Cambodia")
    private String location;
}
