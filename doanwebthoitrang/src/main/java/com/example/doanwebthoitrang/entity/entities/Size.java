package com.example.doanwebthoitrang.entity.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sizes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 20)
    private String name;

    // Inverse Relationship (optional)
    @OneToMany(mappedBy = "size", fetch = FetchType.LAZY)
    private Set<ProductVariant> variants = new HashSet<>();
}
