package com.lumastyle.order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AddressType type;  // BILLING or SHIPPING

    @NotBlank
    private String street;

    @NotBlank
    private String city;

    @NotBlank
    @Column(name = "postal_code")
    private String postalCode;

    @NotBlank
    private String country;

    // getters & setters
}