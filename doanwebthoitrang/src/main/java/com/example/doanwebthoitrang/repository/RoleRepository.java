package com.example.doanwebthoitrang.repository;

import com.example.doanwebthoitrang.entity.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    // Tìm role theo tên (ví dụ: "USER")
    Optional<Role> findByName(String name);
}
