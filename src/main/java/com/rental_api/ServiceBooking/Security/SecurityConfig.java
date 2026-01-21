package com.rental_api.ServiceBooking.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // Disable CSRF since we are using JWT
            .csrf(csrf -> csrf.disable())

            // Stateless session
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                // Public Auth endpoint
                .requestMatchers("/auth/**").permitAll()

                // Swagger / OpenAPI
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/swagger-ui/index.html",
                    "/v3/api-docs/**",
                    "/v3/api-docs/swagger-config",
                    "/swagger-resources/**",
                    "/webjars/**"
                ).permitAll()

                // Allow preflight requests for CORS
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Users endpoint
                .requestMatchers("/users/**").authenticated()

                // Provider Requests endpoints
                .requestMatchers(HttpMethod.POST, "/provider-requests/request").authenticated() // user submit request
                .requestMatchers(HttpMethod.GET, "/provider-requests/all").hasRole("ADMIN") // admin view all
                .requestMatchers(HttpMethod.PUT, "/provider-requests/**/status").hasRole("ADMIN") // admin approve/reject

                // All other requests require authentication
                .anyRequest().authenticated()
            )

            // Add JWT filter before Spring Security's UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // BCrypt password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Optional authentication manager
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}
