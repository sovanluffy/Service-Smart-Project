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

    // CUSTOMER
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    // SERVICE
    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceEntity service;

    @Column(nullable = false)
    private LocalDateTime bookingDateTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private String note;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = BookingStatus.PENDING;
        }
        if (bookingDateTime == null) {
            bookingDateTime = LocalDateTime.now();
        }
    }
}
