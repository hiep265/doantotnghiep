package com.example.doanwebthoitrang.service;

import com.example.doanwebthoitrang.entity.entities.Order;
import com.example.doanwebthoitrang.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService extends BaseService<Order, Integer> {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        super(orderRepository);
        this.orderRepository = orderRepository;
    }

    // Add custom business logic methods here
} 