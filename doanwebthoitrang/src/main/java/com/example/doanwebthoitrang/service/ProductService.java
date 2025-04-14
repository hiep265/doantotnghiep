package com.example.doanwebthoitrang.service;

import com.example.doanwebthoitrang.entity.entities.Product;
import com.example.doanwebthoitrang.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends BaseService<Product, Integer> {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        super(productRepository);
        this.productRepository = productRepository;
    }

    // Add custom business logic methods here
    public Page<Product> searchProducts(String query, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query, pageable);
    }
} 