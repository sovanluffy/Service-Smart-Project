package com.rental_api.ServiceBooking.Dto.Response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@Builder
public class AuthResponse {

    @Schema(description = "ID of the user", example = "1")
    private Long userId;

    @Schema(description = "Full name of the user", example = "Sok Dara")
    private String fullname;

    @Schema(description = "Email address of the user", example = "dara@gmail.com")
    private String email;

    @Schema(description = "Response message", example = "Registered successfully")
    private String message;

    // Optional for JWT login
    @Schema(description = "JWT Token (for login only)", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}
