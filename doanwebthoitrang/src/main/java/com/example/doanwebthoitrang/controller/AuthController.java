package com.example.doanwebthoitrang.controller;

import com.example.doanwebthoitrang.DTO.ApiResponse;
import com.example.doanwebthoitrang.DTO.LoginRequest;
import com.example.doanwebthoitrang.DTO.LoginResponse;
import com.example.doanwebthoitrang.Exception.UserAlreadyExistsException;
import com.example.doanwebthoitrang.services.JwtTokenService;
import com.example.doanwebthoitrang.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.doanwebthoitrang.DTO.RegisterRequest;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {



    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // 1. Xác thực người dùng
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getPhoneNumber(),
                            loginRequest.getPassword()
                    )
            );

            // 2. Nếu thành công, tạo token
            log.info("Authentication successful for user: {}", authentication.getName());
            String jwt = jwtTokenService.generateToken(authentication);

            // 3. Chuẩn bị response thành công
            LoginResponse loginResponse = new LoginResponse(jwt);
            ApiResponse<LoginResponse> response = ApiResponse.success(
                    loginResponse,
                    "Authentication successful",
                    HttpStatus.OK);
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            // Lỗi sai username/password
            log.warn("Authentication failed for user {}: Invalid credentials", loginRequest.getPhoneNumber());
            ApiResponse<Object> response = ApiResponse.error(
                    "Authentication failed: Invalid username or password.",
                    HttpStatus.UNAUTHORIZED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (AuthenticationException e) {
            // Các lỗi xác thực khác
            log.warn("Authentication failed for user {}: {}", loginRequest.getPhoneNumber(), e.getMessage());
            ApiResponse<Object> response = ApiResponse.error(
                    "Authentication failed: " + e.getMessage(), // Cân nhắc không lộ chi tiết lỗi
                    HttpStatus.UNAUTHORIZED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (Exception e) {
            // Lỗi khác (ví dụ: lỗi tạo token, lỗi hệ thống...)
            log.error("An unexpected error occurred during authentication for user {}", loginRequest.getPhoneNumber(), e);
            ApiResponse<Object> response = ApiResponse.error(
                    "An internal server error occurred. Please try again later.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // --- Endpoint Đăng ký Mới ---
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Gọi service để đăng ký
            userService.registerUser(registerRequest);

            // Tạo response thành công
            ApiResponse<Object> response = ApiResponse.success(
                    null, // Không cần trả về dữ liệu cụ thể, chỉ cần thông báo
                    "User registered successfully!",
                    HttpStatus.CREATED // Mã 201 Created
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (UserAlreadyExistsException e) {
            // Lỗi SĐT đã tồn tại
            ApiResponse<Object> response = ApiResponse.error(
                    e.getMessage(), // Lấy thông báo từ exception
                    HttpStatus.CONFLICT // Mã 409 Conflict
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

        } catch (Exception e) {
            // Các lỗi không mong muốn khác (ví dụ: không tìm thấy Role, lỗi DB...)
            log.error("An unexpected error occurred during registration for phone {}", registerRequest.getPhoneNumber(), e);
            ApiResponse<Object> response = ApiResponse.error(
                    "An internal server error occurred during registration.",
                    HttpStatus.INTERNAL_SERVER_ERROR // Mã 500 Internal Server Error
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

