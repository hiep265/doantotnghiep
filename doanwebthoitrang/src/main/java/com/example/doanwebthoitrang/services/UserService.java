package com.example.doanwebthoitrang.services;

import com.example.doanwebthoitrang.DTO.RegisterRequest;
import com.example.doanwebthoitrang.entity.entities.User;

public interface UserService {
    User registerUser(RegisterRequest registerRequest);
}