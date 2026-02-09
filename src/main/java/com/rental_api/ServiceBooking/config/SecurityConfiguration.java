package com.rental_api.ServiceBooking.config;

import com.rental_api.ServiceBooking.Security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        // 0️⃣ Allow OPTIONS for CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 1️⃣ Public endpoints
                        .requestMatchers(
                                "/auth/**",
                                "/api/v1/auth-service/**",
                                "/instances",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        //Category endpoints
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")

                        // 2️⃣ Authenticated endpoints
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/provider-requests/request").authenticated()

                        // 3️⃣ Role-based endpoints
                        .requestMatchers(HttpMethod.GET, "/provider-requests/all").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/provider-requests/*/status").hasRole("ADMIN")

                         // 3️⃣ Booking endpoints
                        // Booking endpoints
                        .requestMatchers(HttpMethod.POST, "/api/services/*/bookings").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/bookings/my").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/api/bookings/*/accept").hasRole("PROVIDER")
                        .requestMatchers(HttpMethod.PUT, "/api/bookings/*/reject").hasRole("PROVIDER")
                        .requestMatchers(HttpMethod.GET, "/api/bookings/all").hasRole("ADMIN")


                        // 4️⃣ Service endpoints (allow authenticated users)
                        .requestMatchers("/api/services/**").authenticated()  // ✅ Add this line

                        // 5️⃣ Catch-all: require authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                );


        return http.build();
    }
}
