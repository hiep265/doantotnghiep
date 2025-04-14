package com.example.doanwebthoitrang.dto;

import java.time.LocalDateTime;

// Using Lombok annotations for boilerplate code reduction
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Integer id;
    private int rating;
    private String comment;
    private String userName; // Assuming Review entity has a User relationship
    private String productName; // Assuming Review entity has a Product relationship
    private LocalDateTime createdAt;
} 