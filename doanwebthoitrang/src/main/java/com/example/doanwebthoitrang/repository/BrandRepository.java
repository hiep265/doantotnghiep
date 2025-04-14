package com.example.doanwebthoitrang.repository;

import com.example.doanwebthoitrang.entity.entities.Brand;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends BaseRepository<Brand, Integer> {
    // Add custom query methods here if needed
} 