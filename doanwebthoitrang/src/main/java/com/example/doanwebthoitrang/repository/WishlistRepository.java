package com.example.doanwebthoitrang.repository;

import com.example.doanwebthoitrang.entity.entities.Wishlist;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends BaseRepository<Wishlist, Integer> {
    // Add custom query methods here if needed
} 