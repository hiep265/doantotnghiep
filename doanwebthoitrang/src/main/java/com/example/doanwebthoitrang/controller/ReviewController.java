package com.example.doanwebthoitrang.controller;

import com.example.doanwebthoitrang.DTO.ApiResponse;
import com.example.doanwebthoitrang.dto.ReviewDTO;
import com.example.doanwebthoitrang.entity.entities.Product;
import com.example.doanwebthoitrang.entity.entities.Review;
import com.example.doanwebthoitrang.entity.entities.User;
import com.example.doanwebthoitrang.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController extends BaseController<Review, Integer> {
    public ReviewController(ReviewService reviewService) {
        super(reviewService);
    }

    @Override
    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<Page<ReviewDTO>>> findAll(Pageable pageable) {
        Page<Review> reviewPage = service.findAll(pageable);
        Page<ReviewDTO> reviewDTOPage = reviewPage.map(this::convertToDto);
        return ResponseEntity.ok(ApiResponse.success(reviewDTOPage, "Reviews retrieved successfully", HttpStatus.OK));
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<ReviewDTO>> findById(@PathVariable Integer id) {
        Optional<Review> reviewOptional = service.findById(id);
        return reviewOptional.map(review -> ResponseEntity.ok(ApiResponse.success(convertToDto(review), "Review retrieved successfully", HttpStatus.OK)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Review not found", HttpStatus.NOT_FOUND)));
    }

    private ReviewDTO convertToDto(Review review) {
        if (review == null) {
            return null;
        }
        User user = review.getUser();
        Product product = review.getProduct();

        String userName = (user != null) ? user.getUsername() : null;
        String productName = (product != null) ? product.getName() : null;

        return new ReviewDTO(
                review.getId(),
                review.getRating(),
                review.getComment(),
                userName,
                productName,
                review.getCreatedAt()
        );
    }
} 