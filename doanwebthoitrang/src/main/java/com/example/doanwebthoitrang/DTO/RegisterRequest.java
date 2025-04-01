package com.example.doanwebthoitrang.DTO;

import com.example.doanwebthoitrang.anotation.validate_password.PasswordMatches;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@PasswordMatches // Áp dụng validator kiểm tra password khớp nhau
public class RegisterRequest {

    @NotBlank(message = "Fullname cannot be blank")
    @Size(max = 100, message = "Fullname must not exceed 100 characters")
    private String fullname;

    @NotBlank(message = "Phone number cannot be blank")
    @Size(max = 15, message = "Phone number must not exceed 15 characters")
    // Có thể thêm @Pattern nếu bạn có định dạng số điện thoại cụ thể
    private String phoneNumber;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    @NotBlank(message = "Confirm password cannot be blank")
    private String confirmPassword; // Trường này chỉ dùng để validation

    @NotNull(message = "Date of birth cannot be null")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    // Getters/Setters được tạo bởi Lombok
}
