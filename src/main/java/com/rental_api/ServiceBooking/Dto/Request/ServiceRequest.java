package com.rental_api.ServiceBooking.Dto.Request;

import lombok.Data;

@Data
public class ServiceRequest {
    private String name;
    private String description;
    private Double price;
    private Integer duration; // duration in minutes
}
