package com.rental_api.ServiceBooking.Repository;

import com.rental_api.ServiceBooking.Entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    // findById already exists, no custom method needed
}
