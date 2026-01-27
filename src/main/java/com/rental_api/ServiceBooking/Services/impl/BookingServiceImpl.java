package com.rental_api.ServiceBooking.Services.impl;

import com.rental_api.ServiceBooking.Dto.Request.BookingRequest;
import com.rental_api.ServiceBooking.Dto.Response.BookingResponse;
import com.rental_api.ServiceBooking.Entity.Booking;
import com.rental_api.ServiceBooking.Entity.ServiceEntity;
import com.rental_api.ServiceBooking.Entity.User;
import com.rental_api.ServiceBooking.Entity.Enum.BookingStatus;
import com.rental_api.ServiceBooking.Exception.BookingNotFoundException;
import com.rental_api.ServiceBooking.Exception.ServiceNotFoundException;
import com.rental_api.ServiceBooking.Repository.BookingRepository;
import com.rental_api.ServiceBooking.Repository.ServiceRepository;
import com.rental_api.ServiceBooking.Repository.UserRepository;
import com.rental_api.ServiceBooking.Services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    @Override
    public BookingResponse createBooking(BookingRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ServiceEntity service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ServiceNotFoundException("Service not found"));

        Booking booking = Booking.builder()
                .user(user)
                .service(service)
                .note(request.getNote())
                .status(BookingStatus.valueOf(String.valueOf(BookingStatus.PENDING)))
                .bookingDate(LocalDateTime.now())
                .build();

        return mapToResponse(bookingRepository.save(booking));
    }

    @Override
    public List<BookingResponse> getMyBookings() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return bookingRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BookingResponse accept(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        booking.setStatus(BookingStatus.ACCEPTED);
        return mapToResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingResponse reject(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        booking.setStatus(BookingStatus.REJECTED);
        return mapToResponse(bookingRepository.save(booking));
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .serviceName(booking.getService().getName())
                .userEmail(booking.getUser().getEmail())
                .bookingDate(booking.getBookingDate())
                .note(booking.getNote())            // actual note
                .status(BookingStatus.valueOf(booking.getStatus().name()))
                .build();
    }
}
