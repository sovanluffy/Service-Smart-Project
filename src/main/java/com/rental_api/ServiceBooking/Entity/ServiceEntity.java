package com.rental_api.ServiceBooking.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "services")
public class ServiceEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private ServiceProvider provider;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
