package com.rental_api.ServiceBooking.Dtos.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Role response object")
public class RoleResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "ADMIN")
    private String name;

    @Schema(example = "Administrator role")
    private String description;
}
