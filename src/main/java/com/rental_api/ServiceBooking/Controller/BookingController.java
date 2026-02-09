package com.rental_api.ServiceBooking.Controller;

import com.rental_api.ServiceBooking.Dto.Request.BookingRequest;
import com.rental_api.ServiceBooking.Dto.Response.ApiResponse;
import com.rental_api.ServiceBooking.Dto.Response.BookingResponse;
import com.rental_api.ServiceBooking.Services.BookingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // Create a booking for a service
    @PostMapping("/{serviceId}/bookings")
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @PathVariable Long serviceId,
            @RequestBody BookingRequest request,
            HttpServletRequest httpRequest
    ) {
        BookingResponse response = bookingService.createBooking(serviceId, request, httpRequest);
        return ResponseEntity.ok(ApiResponse.success(response, "Booking created successfully"));
    }

    // Get current user's bookings
    @GetMapping("/bookings/my")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookings(HttpServletRequest httpRequest) {
        List<BookingResponse> bookings = bookingService.getMyBookings(httpRequest);
        return ResponseEntity.ok(ApiResponse.success(bookings, "My bookings retrieved"));
    }

    // Accept a booking (for provider)
    @PutMapping("/bookings/{bookingId}/accept")
    public ResponseEntity<ApiResponse<BookingResponse>> acceptBooking(
            @PathVariable Long bookingId,
            HttpServletRequest httpRequest
    ) {
        BookingResponse response = bookingService.accept(bookingId, httpRequest);
        return ResponseEntity.ok(ApiResponse.success(response, "Booking accepted"));
    }

    // Reject a booking (for provider)
    @PutMapping("/bookings/{bookingId}/reject")
    public ResponseEntity<ApiResponse<BookingResponse>> rejectBooking(
            @PathVariable Long bookingId,
            HttpServletRequest httpRequest
    ) {
        BookingResponse response = bookingService.reject(bookingId, httpRequest);
        return ResponseEntity.ok(ApiResponse.success(response, "Booking rejected"));
    }

    // Get all bookings (for admin)
    @GetMapping("/bookings/all")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getAllBookings() {
        List<BookingResponse> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(ApiResponse.success(bookings, "All bookings retrieved"));
    }
}
