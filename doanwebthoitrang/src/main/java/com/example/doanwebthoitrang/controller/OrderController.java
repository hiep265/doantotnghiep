package com.example.doanwebthoitrang.controller;

import com.example.doanwebthoitrang.entity.entities.Order;
import com.example.doanwebthoitrang.service.OrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController extends BaseController<Order, Integer> {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        super(orderService);
        this.orderService = orderService;
    }

    // Add custom endpoints here if needed
} 