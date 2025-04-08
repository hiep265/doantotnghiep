package com.example.doanwebthoitrang.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Áp dụng cho tất cả các đường dẫn
                // Thay vì allowedOrigins, sử dụng allowedOriginPatterns
                .allowedOriginPatterns("*") // Cho phép tất cả các mẫu nguồn gốc
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các phương thức được phép
                .allowedHeaders("*") // Cho phép tất cả các header
                .allowCredentials(true); // Cho phép gửi kèm credentials (quan trọng cho JWT/Session)
        // .maxAge(3600); // Optional: Thời gian cache kết quả preflight (giây)
    }
}