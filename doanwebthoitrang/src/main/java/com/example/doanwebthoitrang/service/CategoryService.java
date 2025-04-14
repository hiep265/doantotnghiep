package com.example.doanwebthoitrang.service;

import com.example.doanwebthoitrang.entity.entities.Category;
import com.example.doanwebthoitrang.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends BaseService<Category, Integer> {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
    }

    // Add custom business logic methods here
} 