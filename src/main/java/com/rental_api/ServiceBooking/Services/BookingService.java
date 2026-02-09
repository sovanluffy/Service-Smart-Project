package com.rental_api.ServiceBooking.Services;

import com.rental_api.ServiceBooking.Dto.Request.BookingRequest;
import com.rental_api.ServiceBooking.Dto.Response.BookingResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface BookingService {

    // Create a booking
    BookingResponse createBooking(Long serviceId, BookingRequest request, HttpServletRequest requestHttp);

    // Get bookings for current user
    List<BookingResponse> getMyBookings(HttpServletRequest request);

    // Accept a booking (for provider)
    BookingResponse accept(Long bookingId, HttpServletRequest request);

    // Reject a booking (for provider)
    BookingResponse reject(Long bookingId, HttpServletRequest request);

    // Get all bookings (for admin)
    List<BookingResponse> getAllBookings();
}
