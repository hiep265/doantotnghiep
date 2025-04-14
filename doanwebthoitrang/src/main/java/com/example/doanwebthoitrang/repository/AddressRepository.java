package com.example.doanwebthoitrang.repository;

import com.example.doanwebthoitrang.entity.entities.Address;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends BaseRepository<Address, Integer> {
    // Add custom query methods here if needed
} 