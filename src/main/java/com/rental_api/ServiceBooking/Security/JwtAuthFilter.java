package com.rental_api.ServiceBooking.Security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        
        String path = request.getRequestURI();

        // 1. FAST PASS: Bypass Swagger and Auth immediately to prevent 403 errors
        if (path.contains("/auth") || 
            path.contains("/v3/api-docs") || 
            path.contains("/swagger-ui") || 
            path.contains("/swagger-config")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. EXTRACT TOKEN: Check for Authorization header
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        // 3. VALIDATE AND SET AUTHENTICATION
        try {
            if (jwtUtils.validateToken(token)) {
                Claims claims = jwtUtils.getClaims(token);
                String username = claims.getSubject();
                
                // Get roles from the token (assumes roles are stored as a List in JWT)
                @SuppressWarnings("unchecked")
                List<String> roles = claims.get("roles", List.class);
                
                // Convert roles to Spring Authorities
                List<SimpleGrantedAuthority> authorities = roles != null ? 
                    roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList() : 
                    List.of();

                // Create Authentication object for Spring Security
                UsernamePasswordAuthenticationToken auth = 
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
                
                // Link request details (IP, Session ID) to the auth object
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Save the user to the Security Context for the rest of the request
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            // If token is expired or malformed, ensure context is clean
            SecurityContextHolder.clearContext();
        }

        // 4. CONTINUE: Move to the next filter in the chain
        filterChain.doFilter(request, response);
    }
}