package com.rental_api.ServiceBooking.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ServiceResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration; // in minutes
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
