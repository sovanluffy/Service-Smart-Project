package com.rental_api.ServiceBooking.Services.impl;

import com.rental_api.ServiceBooking.Dto.Request.BookingRequest;
import com.rental_api.ServiceBooking.Dto.Response.BookingResponse;
import com.rental_api.ServiceBooking.Entity.Booking;
import com.rental_api.ServiceBooking.Entity.ServiceEntity;
import com.rental_api.ServiceBooking.Entity.User;
import com.rental_api.ServiceBooking.Entity.Enum.BookingStatus;
import com.rental_api.ServiceBooking.Repository.BookingRepository;
import com.rental_api.ServiceBooking.Repository.ServiceRepository;
import com.rental_api.ServiceBooking.Security.JwtUtils;
import com.rental_api.ServiceBooking.Services.BookingService;
import com.rental_api.ServiceBooking.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ServiceRepository serviceRepository;
    private final UserService userService;

    @Override
    public BookingResponse createBooking(Long serviceId, BookingRequest request) {

        // Get logged-in customer
        User customer = UserService.getCurrentUser();

        // Get service
        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Service not found"
                ));

        // Check if already booked
        boolean alreadyBooked = bookingRepository.existsByCustomerIdAndServiceIdAndStatusIn(
                customer.getId(),
                serviceId,
                List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED)
        );

        if (alreadyBooked) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "You already booked this service. Please wait for confirmation."
            );
        }

        // Build and save booking
        Booking booking = Booking.builder()
                .customer(customer)
                .service(service)
                .bookingDateTime(request.getBookingDateTime())
                .note(request.getNote())
                .status(BookingStatus.PENDING)
                .build();

        bookingRepository.save(booking);

        return mapToResponse(booking);
    }

    @Override
    public List<BookingResponse> getMyBookings() {
        User customer = UserService.getCurrentUser();

        return bookingRepository.findByCustomerId(customer.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BookingResponse accept(Long bookingId) {
        Booking booking = getBookingForProvider(bookingId);
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
        return mapToResponse(booking);
    }

    @Override
    public BookingResponse reject(Long bookingId) {
        Booking booking = getBookingForProvider(bookingId);
        booking.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking);
        return mapToResponse(booking);
    }

    private Booking getBookingForProvider(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Booking not found"
                ));

        User provider = UserService.getCurrentUser();

        if (!booking.getService().getProvider().getId().equals(provider.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your booking");
        }

        return booking;
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .serviceName(booking.getService().getName())
                .userEmail(booking.getCustomer().getEmail())
                .status(booking.getStatus())
                .bookingDateTime(booking.getBookingDateTime())
                .note(booking.getNote())
                .build();
    }
}
