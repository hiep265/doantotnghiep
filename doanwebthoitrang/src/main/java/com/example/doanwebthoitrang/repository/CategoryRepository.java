package com.example.doanwebthoitrang.repository;

import com.example.doanwebthoitrang.entity.entities.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends BaseRepository<Category, Integer> {
    // Add custom query methods here if needed
} 