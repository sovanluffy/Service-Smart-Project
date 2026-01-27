package com.rental_api.ServiceBooking.Entity;

import com.rental_api.ServiceBooking.Entity.Enum.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // who books
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // what service
    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceEntity service;

    private LocalDateTime bookingDate;



    private String note;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

   

    // Automatically set default status when saving for the first time
    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = BookingStatus.PENDING; // default status
        }
    }
}
