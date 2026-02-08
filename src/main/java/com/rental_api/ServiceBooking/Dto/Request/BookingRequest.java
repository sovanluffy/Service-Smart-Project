package com.rental_api.ServiceBooking.Dto.Request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingRequest {
    private LocalDateTime bookingDateTime; // Customer chooses this
    private String note;
}
