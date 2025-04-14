package com.example.doanwebthoitrang.controller;

import com.example.doanwebthoitrang.entity.entities.Brand;
import com.example.doanwebthoitrang.service.BrandService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/brands")
public class BrandController extends BaseController<Brand, Integer> {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        super(brandService);
        this.brandService = brandService;
    }

    // Add custom endpoints here if needed
} 