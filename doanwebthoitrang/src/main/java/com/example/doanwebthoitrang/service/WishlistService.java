package com.example.doanwebthoitrang.service;

import com.example.doanwebthoitrang.entity.entities.Wishlist;
import com.example.doanwebthoitrang.repository.WishlistRepository;
import org.springframework.stereotype.Service;

@Service
public class WishlistService extends BaseService<Wishlist, Integer> {
    private final WishlistRepository wishlistRepository;

    public WishlistService(WishlistRepository wishlistRepository) {
        super(wishlistRepository);
        this.wishlistRepository = wishlistRepository;
    }

    // Add custom business logic methods here
} 