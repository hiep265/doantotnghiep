package com.example.doanwebthoitrang.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String imageUrl;
    private String categoryName; // Assuming relationship with Category
    private String brandName;    // Assuming relationship with Brand
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Optional: Add fields for average rating or number of reviews if needed
    // private Double averageRating;
    // private Integer reviewCount;
} 