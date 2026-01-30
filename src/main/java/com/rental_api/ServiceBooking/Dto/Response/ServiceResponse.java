package com.rental_api.ServiceBooking.Dto.Response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ServiceResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private String providerName; // from User.fullName
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
