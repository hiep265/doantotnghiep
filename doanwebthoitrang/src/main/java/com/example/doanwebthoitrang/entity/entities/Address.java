package com.example.doanwebthoitrang.entity.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "fullname", nullable = false, length = 100)
    private String fullname;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "street_address", nullable = false, length = 255)
    private String streetAddress;

    @Column(name = "ward", nullable = false, length = 100)
    private String ward;

    @Column(name = "district", nullable = false, length = 100)
    private String district;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "country", length = 100)
    private String country = "Vietnam";

    @Column(name = "is_default", nullable = false)
    private boolean defaultAddress = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Inverse side for orders (optional)
    @OneToMany(mappedBy = "shippingAddress", fetch = FetchType.LAZY)
    private Set<Order> ordersShippedTo = new HashSet<>();

    // Inverse side for user default address (optional)
    @OneToOne(mappedBy = "defaultAddress", fetch = FetchType.LAZY)
    private User userDefault;
}
