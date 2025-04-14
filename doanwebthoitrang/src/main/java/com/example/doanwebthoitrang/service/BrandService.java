package com.example.doanwebthoitrang.service;

import com.example.doanwebthoitrang.entity.entities.Brand;
import com.example.doanwebthoitrang.repository.BrandRepository;
import org.springframework.stereotype.Service;

@Service
public class BrandService extends BaseService<Brand, Integer> {
    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        super(brandRepository);
        this.brandRepository = brandRepository;
    }

    // Add custom business logic methods here
} 