package com.rental_api.ServiceBooking.Services.impl;

import com.rental_api.ServiceBooking.Dto.Request.LoginRequest;
import com.rental_api.ServiceBooking.Dto.Request.RegisterRequest;
import com.rental_api.ServiceBooking.Dto.Response.AuthResponse;
import com.rental_api.ServiceBooking.Entity.Role;
import com.rental_api.ServiceBooking.Entity.User;
import com.rental_api.ServiceBooking.Exception.ConflictException;
import com.rental_api.ServiceBooking.Exception.InvalidInputException;
import com.rental_api.ServiceBooking.Repository.RoleRepository;
import com.rental_api.ServiceBooking.Repository.UserRepository;
import com.rental_api.ServiceBooking.Services.AuthService;
import com.rental_api.ServiceBooking.Security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    // ------------------- REGISTER CUSTOMER -------------------
    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering CUSTOMER: {}", request.getEmail());

        validateEmail(request.getEmail());
        checkEmailExists(request.getEmail());

        User user = User.builder()
                .fullname(request.getFullname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .location(request.getLocation())
                .build();

        Role customerRole = roleRepository.findByName("CUSTOMER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("CUSTOMER");
                    r.setDescription("Default CUSTOMER role");
                    return roleRepository.save(r);
                });

        user.setRoles(Set.of(customerRole));
        user = userRepository.save(user);

        return buildAuthResponse(user, "Registered successfully");
    }

    

    // ------------------- LOGIN -------------------
    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt: {}", request.getEmail());

        validateEmail(request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return buildAuthResponse(user, "Login successful");
    }

    // ------------------- LOGOUT -------------------
    @Override
    @Transactional
    public void logout(String token) {
        log.info("Logging out token: {}", token);
        // Stateless JWT: frontend just deletes token
        // Optional: store in blacklist for server invalidation
    }

    // ------------------- PRIVATE HELPERS -------------------
    private void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidInputException("Invalid email format");
        }
    }

    private void checkEmailExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("Email already exists");
        }
    }

    private AuthResponse buildAuthResponse(User user, String message) {
        List<String> roleNames = user.getRoles().stream().map(Role::getName).toList();
        List<Long> roleIds = user.getRoles().stream().map(Role::getId).toList();

        String token = jwtUtils.generateToken(user.getId(), user.getEmail(), user.getEmail(), roleNames, roleIds);

        return AuthResponse.builder()
                .userId(user.getId())
                .fullname(user.getFullname())
                .email(user.getEmail())
                .message(message)
                .token(token)
                .roles(roleNames)
                .roleIds(roleIds)
                .build();
    }
}
