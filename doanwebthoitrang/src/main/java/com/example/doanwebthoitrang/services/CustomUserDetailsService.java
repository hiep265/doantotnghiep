package com.example.doanwebthoitrang.services;

import com.example.doanwebthoitrang.entity.entities.User;
import com.example.doanwebthoitrang.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service // Đánh dấu là Spring Bean
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true) // Đọc dữ liệu nên dùng readOnly
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        // Tham số username ở đây chính là phoneNumber được truyền vào từ AuthenticationManager
        User userEntity = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with phone number: " + phoneNumber));

        // Lấy quyền từ role của user entity
        Set<GrantedAuthority> authorities = new HashSet<>();
        // Giả sử User entity có trường 'role' là đối tượng Role có trường 'name'
        if (userEntity.getRole() != null) {
            // Quan trọng: Thêm tiền tố "ROLE_" nếu JwtAuthenticationConverter của bạn cần
            // hoặc nếu bạn dùng @PreAuthorize("hasRole('...')")
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().getName().toUpperCase()));
            // Nếu không cần tiền tố ROLE_ (ví dụ dùng @PreAuthorize("hasAuthority('...')"))
            // authorities.add(new SimpleGrantedAuthority(userEntity.getRole().getName().toUpperCase()));
        } else {
            // Xử lý trường hợp user không có role nếu cần
            // authorities.add(new SimpleGrantedAuthority("ROLE_DEFAULT")); // Ví dụ
        }


        // Trả về đối tượng UserDetails mà Spring Security hiểu được
        return new org.springframework.security.core.userdetails.User(
                userEntity.getPhoneNumber(), // Username là số điện thoại
                userEntity.getPassword(),    // Mật khẩu đã mã hóa
                userEntity.isActive(),       // enabled (từ trường is_active)
                true,                        // accountNonExpired (mặc định là true)
                true,                        // credentialsNonExpired (mặc định là true)
                true,                        // accountNonLocked (mặc định là true)
                authorities                  // Danh sách quyền
        );
    }
}
