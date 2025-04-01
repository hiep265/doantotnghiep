package com.example.doanwebthoitrang.anotation.validate_password;

import com.example.doanwebthoitrang.DTO.RegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        // Không cần khởi tạo gì đặc biệt
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        // Chỉ kiểm tra nếu đối tượng là RegisterRequest
        if (obj instanceof RegisterRequest request) {
            String password = request.getPassword();
            String confirmPassword = request.getConfirmPassword();
            // Mật khẩu có thể null nếu các validation khác chưa chạy, nên cần kiểm tra
            return password != null && password.equals(confirmPassword);
        }
        // Nếu không phải RegisterRequest thì mặc định là hợp lệ (để annotation dùng được ở nơi khác nếu cần)
        return true;
    }
}