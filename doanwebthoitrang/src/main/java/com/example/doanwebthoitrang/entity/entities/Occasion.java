package com.example.doanwebthoitrang.entity.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "occasions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Occasion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @ManyToMany(mappedBy = "occasions", fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();
}
