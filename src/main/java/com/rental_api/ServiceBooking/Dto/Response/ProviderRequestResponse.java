package com.rental_api.ServiceBooking.Dto.Response;

import lombok.Data;

@Data
public class ProviderRequestResponse {
    private Long id;
    private Long userId;
    private String fullname;
    private String email;
    private String bio;
    private Integer experience;
    private String status; // PENDING / APPROVED / REJECTED
}