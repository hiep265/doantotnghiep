package com.example.doanwebthoitrang.entity.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "colors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "hex_code", unique = true, length = 7)
    private String hexCode;

    // Inverse Relationship (optional)
    @OneToMany(mappedBy = "color", fetch = FetchType.LAZY)
    private Set<ProductVariant> variants = new HashSet<>();
}
