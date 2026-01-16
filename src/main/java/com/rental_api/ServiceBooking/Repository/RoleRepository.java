package com.rental_api.ServiceBooking.Repository;

import com.rental_api.ServiceBooking.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> { }
