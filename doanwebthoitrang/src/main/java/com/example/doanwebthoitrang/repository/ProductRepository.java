package com.example.doanwebthoitrang.repository;

import com.example.doanwebthoitrang.entity.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends BaseRepository<Product, Integer> {
    // Add custom query methods here if needed
    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String nameQuery, String descriptionQuery, Pageable pageable);
} 