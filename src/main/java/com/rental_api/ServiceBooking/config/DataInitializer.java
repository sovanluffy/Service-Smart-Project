package com.rental_api.ServiceBooking.config;

import com.rental_api.ServiceBooking.Entity.Role;
import com.rental_api.ServiceBooking.Entity.User;
import com.rental_api.ServiceBooking.Entity.UserRole;
import com.rental_api.ServiceBooking.Repository.RoleRepository;
import com.rental_api.ServiceBooking.Repository.UserRepository;
import com.rental_api.ServiceBooking.Repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // =========================
        // 1. Create ADMIN Role
        // =========================
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ADMIN");
                    role.setDescription("System Administrator");
                    return roleRepository.save(role);
                });

        // =========================
        // 2. Create ADMIN User
        // =========================
        String adminEmail = "admin@example.com";

        User adminUser = userRepository.findByEmail(adminEmail)
                .orElseGet(() -> {
                    User user = new User();
                    user.setFullname("System Admin");
                    user.setEmail(adminEmail);
                    user.setPassword(passwordEncoder.encode("admin123"));
                    user.setPhone("000000000");
                    user.setAddress("System");
                    user.setLocation("System");
                    return userRepository.save(user);
                });

        // =========================
        // 3. Assign ADMIN Role
        // =========================
        boolean hasRole = userRoleRepository
                .existsByUserAndRole(adminUser, adminRole);

        if (!hasRole) {
            UserRole userRole = new UserRole();
            userRole.setUser(adminUser);
            userRole.setRole(adminRole);
            userRoleRepository.save(userRole);
        }

        log.info("✅ ADMIN user seeded successfully");
    }
}
