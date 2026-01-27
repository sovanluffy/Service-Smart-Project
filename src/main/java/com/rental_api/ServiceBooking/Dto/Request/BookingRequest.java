package com.rental_api.ServiceBooking.Dto.Request;

import lombok.Data;

@Data
public class BookingRequest {
    private Long serviceId;
    private String note;
    private String bookingDateTime;
}
