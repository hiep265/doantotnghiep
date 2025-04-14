package com.example.doanwebthoitrang.repository;

import com.example.doanwebthoitrang.entity.entities.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends BaseRepository<Order, Integer> {
    // Add custom query methods here if needed
} 