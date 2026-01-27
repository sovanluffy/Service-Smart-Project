package com.rental_api.ServiceBooking.Services;

import com.rental_api.ServiceBooking.Dto.Request.BookingRequest;
import com.rental_api.ServiceBooking.Dto.Response.BookingResponse;

import java.util.List;

public interface BookingService {

    BookingResponse createBooking(BookingRequest request);

    List<BookingResponse> getMyBookings();

    List<BookingResponse> getAllBookings();

    BookingResponse accept(Long bookingId);

    BookingResponse reject(Long bookingId);
}
