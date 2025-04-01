package com.example.doanwebthoitrang.repository;

import com.example.doanwebthoitrang.entity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // Kiểm tra xem SĐT đã tồn tại chưa
    boolean existsByPhoneNumber(String phoneNumber);

    // Optional: Tìm user bằng SĐT (có thể dùng cho việc khác)
    Optional<User> findByPhoneNumber(String phoneNumber);
}