package com.rental_api.ServiceBooking.Entity;

import com.rental_api.ServiceBooking.Entity.Enum.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // matches DB column
    private User customer;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceEntity service;

    @Column(name = "booking_date_time", nullable = false)
    private LocalDateTime bookingDateTime;

    private String note;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
