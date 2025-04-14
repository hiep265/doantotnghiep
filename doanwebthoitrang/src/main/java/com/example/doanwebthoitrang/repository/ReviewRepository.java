package com.example.doanwebthoitrang.repository;

import com.example.doanwebthoitrang.entity.entities.Review;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends BaseRepository<Review, Integer> {
    // Add custom query methods here if needed
} 