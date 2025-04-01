package com.example.doanwebthoitrang.entity.entities;

import com.example.doanwebthoitrang.entity.enums.ApplicableTo;
import com.example.doanwebthoitrang.entity.enums.DiscountType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "promotions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", unique = true, length = 50)
    private String code;

    @Column(name = "description", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, length = 20)
    private DiscountType discountType;

    @Column(name = "discount_value", nullable = false, precision = 15, scale = 2)
    private java.math.BigDecimal discountValue;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "max_uses")
    private Integer maxUses;

    @Column(name = "max_uses_per_user")
    private Integer maxUsesPerUser;

    @Column(name = "min_order_value", precision = 15, scale = 2)
    private java.math.BigDecimal minOrderValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "applicable_to", nullable = false, length = 30)
    private ApplicableTo applicableTo = ApplicableTo.all;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationships for specific applicability
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "promotion_categories",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> applicableCategories = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "promotion_products",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> applicableProducts = new HashSet<>();

}
