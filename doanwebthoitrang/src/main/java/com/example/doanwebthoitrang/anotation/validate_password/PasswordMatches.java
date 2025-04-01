package com.example.doanwebthoitrang.anotation.validate_password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE}) // Áp dụng cho class
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class) // Liên kết với validator
@Documented
public @interface PasswordMatches {
    String message() default "Passwords do not match"; // Thông báo lỗi mặc định
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
