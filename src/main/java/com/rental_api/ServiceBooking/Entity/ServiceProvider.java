package com.rental_api.ServiceBooking.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "service_providers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String bio;

    private Double experience; // Changed from Integer to Double

    private Float rating;
}
