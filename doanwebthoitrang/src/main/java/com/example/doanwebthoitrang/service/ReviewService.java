package com.example.doanwebthoitrang.service;

import com.example.doanwebthoitrang.entity.entities.Review;
import com.example.doanwebthoitrang.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewService extends BaseService<Review, Integer> {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        super(reviewRepository);
        this.reviewRepository = reviewRepository;
    }

    // Add custom business logic methods here
} 