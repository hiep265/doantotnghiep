package com.example.doanwebthoitrang.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder // Dùng Builder pattern để tạo đối tượng dễ dàng hơn
@JsonInclude(JsonInclude.Include.NON_NULL) // Không bao gồm các trường null trong JSON output
public class ApiResponse<T> {

    private String status; // ví dụ: "success", "error"
    private String message;
    private T data; // Dữ liệu trả về (có thể là LoginResponse, hoặc null...)
    private int statusCode; // Mã trạng thái HTTP

    // --- Factory methods tiện ích ---

    public static <T> ApiResponse<T> success(T data, String message, HttpStatus status) {
        return ApiResponse.<T>builder()
                .status("success")
                .message(message)
                .data(data)
                .statusCode(status.value())
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return success(data, message, HttpStatus.OK);
    }

    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Operation successful", HttpStatus.OK);
    }

    public static <T> ApiResponse<T> error(String message, HttpStatus status) {
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .data(null) // Không có data khi lỗi
                .statusCode(status.value())
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return error(message, HttpStatus.BAD_REQUEST); // Mặc định Bad Request nếu không chỉ định
    }
}