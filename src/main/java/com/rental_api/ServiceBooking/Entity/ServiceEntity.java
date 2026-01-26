package com.rental_api.ServiceBooking.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "services")
@Data
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private ServiceProvider provider;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
