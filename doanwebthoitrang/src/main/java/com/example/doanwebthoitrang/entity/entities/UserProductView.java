package com.example.doanwebthoitrang.entity.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_product_views")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProductView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // BIGINT maps to Long

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Nullable
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @CreationTimestamp // Use creation timestamp for viewed_at
    @Column(name = "viewed_at", nullable = false, updatable = false)
    private LocalDateTime viewedAt;

    @Column(name = "session_id", length = 100)
    private String sessionId;
}
