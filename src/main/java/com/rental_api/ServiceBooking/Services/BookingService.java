package com.rental_api.ServiceBooking.Services;

import com.rental_api.ServiceBooking.Dto.Request.BookingRequest;
import com.rental_api.ServiceBooking.Dto.Response.BookingResponse;

import java.util.List;

public interface BookingService {

    BookingResponse createBooking(Long serviceId, BookingRequest request);

    List<BookingResponse> getMyBookings();

    BookingResponse accept(Long bookingId);

    BookingResponse reject(Long bookingId);
}
