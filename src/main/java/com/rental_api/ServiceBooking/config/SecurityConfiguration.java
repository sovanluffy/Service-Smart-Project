package com.rental_api.ServiceBooking.config;

import com.rental_api.ServiceBooking.config.JwtAccessDeniedHandler;
import com.rental_api.ServiceBooking.config.JwtAuthenticationEntryPoint;
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

                        // 0️⃣ Allow OPTIONS for CORS (important for browser/swagger requests)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 1️⃣ Public endpoints (Registration, Login, and Documentation)
                        .requestMatchers(
                                "/auth/**",                          // ✅ Allows /auth/register and /auth/authenticate
                                "/api/v1/auth-service/**",           // ✅ Allows the versioned paths
                                "/instances",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/api-docs/**",                      // ✅ Fixes Swagger config 401
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // 2️⃣ Authenticated endpoints
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/provider-requests/request").authenticated()

                        // 3️⃣ Role-based endpoints
                        .requestMatchers(HttpMethod.GET, "/provider-requests/all").hasRole("ADMIN")
                        
                        // ✅ Fix: Changed ** to * to satisfy modern PathPatternParser
                        .requestMatchers(HttpMethod.PUT, "/provider-requests/*/status").hasRole("ADMIN")

                        // 4️⃣ Catch-all
                        
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