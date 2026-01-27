package com.rental_api.ServiceBooking.Controller;

import com.rental_api.ServiceBooking.Dto.Request.BookingRequest;
import com.rental_api.ServiceBooking.Dto.Response.BookingResponse;
import com.rental_api.ServiceBooking.Services.BookingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings")
public class BookingController {

    private final BookingService bookingService;

    // CUSTOMER
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<BookingResponse> create(@RequestBody BookingRequest request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    // CUSTOMER
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/my")
    public ResponseEntity<List<BookingResponse>> myBookings() {
        return ResponseEntity.ok(bookingService.getMyBookings());
    }

    // ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<BookingResponse>> all() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    // PROVIDER
    @PreAuthorize("hasRole('PROVIDER')")
    @PatchMapping("/{id}/accept")
    public ResponseEntity<BookingResponse> accept(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.accept(id));
    }

    // PROVIDER
    @PreAuthorize("hasRole('PROVIDER')")
    @PatchMapping("/{id}/reject")
    public ResponseEntity<BookingResponse> reject(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.reject(id));
    }
}
