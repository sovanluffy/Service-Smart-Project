package com.rental_api.ServiceBooking.Dto.Request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServiceRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration; // in minutes
}
