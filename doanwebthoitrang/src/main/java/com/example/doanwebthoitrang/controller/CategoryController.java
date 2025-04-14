package com.example.doanwebthoitrang.controller;

import com.example.doanwebthoitrang.entity.entities.Category;
import com.example.doanwebthoitrang.service.CategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController extends BaseController<Category, Integer> {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        super(categoryService);
        this.categoryService = categoryService;
    }

    // Add custom endpoints here if needed
} 