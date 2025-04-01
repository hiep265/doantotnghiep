package com.example.doanwebthoitrang.services;

import com.example.doanwebthoitrang.DTO.RegisterRequest;
import com.example.doanwebthoitrang.repository.UserRepository;
import com.example.doanwebthoitrang.repository.RoleRepository;
import com.example.doanwebthoitrang.Exception.UserAlreadyExistsException;
import com.example.doanwebthoitrang.entity.entities.User;
import com.example.doanwebthoitrang.entity.entities.Role;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inject PasswordEncoder đã cấu hình

    public static final String USER_ROLE = "user"; // Tên role mặc định

    @Override
    @Transactional // Đảm bảo tất cả thao tác trong hàm này là một transaction
    public User registerUser(RegisterRequest request) {
        // 1. Kiểm tra SĐT đã tồn tại chưa
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            log.warn("Registration attempt failed: Phone number {} already exists", request.getPhoneNumber());
            throw new UserAlreadyExistsException("Phone number " + request.getPhoneNumber() + " is already registered.");
        }

        // 2. Tìm Role "USER"
        // Nên chuẩn hóa tên Role trong DB (vd: USER) và trong code
        Role userRole = roleRepository.findByName(USER_ROLE.toUpperCase()) // Tìm theo tên chuẩn hóa
                .orElseThrow(() -> {
                    log.error("FATAL: Default role '{}' not found in database!", USER_ROLE.toUpperCase());
                    // Đây là lỗi nghiêm trọng, cần báo động hoặc xử lý đặc biệt
                    return new RuntimeException("Default user role not configured.");
                });

        // 3. Mã hóa mật khẩu
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 4. Tạo đối tượng User mới
        User newUser = User.builder()
                .fullname(request.getFullname())
                .phoneNumber(request.getPhoneNumber())
                .password(encodedPassword) // Lưu mật khẩu đã mã hóa
                .dateOfBirth(request.getDateOfBirth())
                .role(userRole) // Gán role mặc định
                .active(true) // Kích hoạt tài khoản ngay
                // createdAt và updatedAt sẽ được @CreationTimestamp/@UpdateTimestamp xử lý
                .build();

        // 5. Lưu vào database
        User savedUser = userRepository.save(newUser);
        log.info("User registered successfully with phone number: {}", savedUser.getPhoneNumber());

        return savedUser; // Trả về user đã lưu (có thể dùng để làm gì đó khác nếu cần)
    }
}
