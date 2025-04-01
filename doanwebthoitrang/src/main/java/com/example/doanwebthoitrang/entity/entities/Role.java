package com.example.doanwebthoitrang.entity.entities;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// --- USER & AUTHENTICATION ---

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    // Inverse side (optional, if you need to find users by role)
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();
}


// --- PRODUCT CATALOG ---




// --- ORDER PROCESSING ---



// --- FEATURES FOR PERSUASION & LLM INPUT ---




