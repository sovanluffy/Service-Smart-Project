package com.rental_api.ServiceBooking.Repository;

import com.rental_api.ServiceBooking.Entity.Booking;
import com.rental_api.ServiceBooking.Entity.Enum.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByCustomerIdAndServiceIdAndStatusIn(Long customerId, Long serviceId, List<BookingStatus> statuses);

    List<Booking> findByCustomerId(Long customerId);
}
