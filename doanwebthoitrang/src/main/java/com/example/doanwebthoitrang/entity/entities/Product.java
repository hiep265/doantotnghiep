package com.example.doanwebthoitrang.entity.entities;

import com.example.doanwebthoitrang.entity.enums.GenderTarget;
import com.example.doanwebthoitrang.entity.enums.Season;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 350)
    private String name;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_target", length = 10)
    private GenderTarget genderTarget;

    @Enumerated(EnumType.STRING)
    @Column(name = "season", length = 20)
    private Season season;

    @Column(name = "slug", unique = true, length = 400)
    private String slug;

    @Column(name = "is_featured", nullable = false)
    private boolean featured = false;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProductVariant> variants = new HashSet<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_materials",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id")
    )
    private Set<Material> materials = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_styles",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "style_id")
    )
    private Set<Style> styles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_occasions",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "occasion_id")
    )
    private Set<Occasion> occasions = new HashSet<>();

    @ManyToMany(mappedBy = "applicableProducts", fetch = FetchType.LAZY)
    private Set<Promotion> promotions = new HashSet<>();
}