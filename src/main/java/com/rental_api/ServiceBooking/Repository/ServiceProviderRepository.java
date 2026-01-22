package com.rental_api.ServiceBooking.Repository;

import com.rental_api.ServiceBooking.Entity.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {
     long countByUserId(Long userId);
}
