package com.example.doanwebthoitrang.controller;

import com.example.doanwebthoitrang.entity.entities.Wishlist;
import com.example.doanwebthoitrang.service.WishlistService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
    @RequestMapping("/api/wishlists")
public class WishlistController extends BaseController<Wishlist, Integer> {
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        super(wishlistService);
        this.wishlistService = wishlistService;
    }

    // Add custom endpoints here if needed
} 