package com.rental_api.ServiceBooking.Dto.Response;

import com.rental_api.ServiceBooking.Entity.Enum.BookingStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponse {
    private Long id;
    private String serviceName;
    private String userEmail;
    private BookingStatus status;
    private LocalDateTime bookingDateTime;
    private String note;
}
