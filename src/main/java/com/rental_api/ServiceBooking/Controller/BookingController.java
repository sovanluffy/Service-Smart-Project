package com.rental_api.ServiceBooking.Controller;

import com.rental_api.ServiceBooking.Dto.Request.BookingRequest;
import com.rental_api.ServiceBooking.Dto.Response.BookingResponse;
import com.rental_api.ServiceBooking.Services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // CUSTOMER
    @PostMapping("/{serviceId}/bookings")
    @PreAuthorize("hasRole('CUSTOMER')")
    public BookingResponse createBooking(@PathVariable Long serviceId,
                                         @RequestBody BookingRequest request) {
        return bookingService.createBooking(serviceId, request);
    }

    @GetMapping("/bookings/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<BookingResponse> myBookings() {
        return bookingService.getMyBookings();
    }

    // PROVIDER
    @PutMapping("/bookings/{id}/accept")
    @PreAuthorize("hasRole('PROVIDER')")
    public BookingResponse accept(@PathVariable Long id) {
        return bookingService.accept(id);
    }

    @PutMapping("/bookings/{id}/reject")
    @PreAuthorize("hasRole('PROVIDER')")
    public BookingResponse reject(@PathVariable Long id) {
        return bookingService.reject(id);
    }
}
