package com.rental_api.ServiceBooking.Repository;

import com.rental_api.ServiceBooking.Entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> { }
