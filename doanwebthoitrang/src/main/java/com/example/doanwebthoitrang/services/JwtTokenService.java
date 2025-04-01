package com.example.doanwebthoitrang.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JwtTokenService {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenService.class);

    @Value("${jwt.secret}")
    private String secretKey;

    // Thời gian token hợp lệ (ví dụ: 1 giờ) - Nên đưa vào application.properties
    @Value("${jwt.expiration-hours}") // Mặc định là 1 giờ nếu không cấu hình
    private long expirationHours;

    // Issuer (Tên định danh của ứng dụng cấp token) - Nên đưa vào application.properties
    @Value("${jwt.issuer}") // Thay bằng issuer của bạn
    private String issuer;

    /**
     * Tạo JWT token dựa trên thông tin Authentication.
     *
     * @param authentication Đối tượng Authentication chứa thông tin người dùng và quyền.
     * @return Chuỗi JWT token đã được ký.
     * @throws JOSEException Nếu có lỗi trong quá trình tạo hoặc ký token.
     */
    public String generateToken(Authentication authentication) throws JOSEException {
        if (authentication == null) {
            throw new IllegalArgumentException("Authentication object cannot be null");
        }

        // 1. Chuẩn bị Signer với Secret Key và thuật toán HS512
        JWSSigner signer = new MACSigner(secretKey.getBytes());

        // 2. Chuẩn bị Claims Set (Payload của JWT)
        Instant now = Instant.now();
        Instant expirationTime = now.plus(expirationHours, ChronoUnit.HOURS);

        // Lấy danh sách quyền (authorities) từ Authentication
        // Bỏ tiền tố "ROLE_" vì JwtAuthenticationConverter của bạn sẽ thêm lại khi giải mã
        List<String> scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> authority.startsWith("ROLE_") ? authority.substring(5) : authority) // Bỏ prefix ROLE_
                .collect(Collectors.toList());

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(authentication.getName()) // sub: Tên người dùng (username)
                .issuer(issuer) // iss: Đơn vị cấp token
                // .audience("your-audience") // aud: Đối tượng nhận token (tùy chọn)
                .issueTime(Date.from(now)) // iat: Thời điểm cấp
                .expirationTime(Date.from(expirationTime)) // exp: Thời điểm hết hạn
                .jwtID(UUID.randomUUID().toString()) // jti: ID duy nhất cho token (tùy chọn)
                .claim("scope", scope) // Custom claim chứa danh sách quyền (theo cấu hình converter của bạn)
                // Thêm các claim khác nếu cần (ví dụ: user ID, full name, email...)
                // .claim("userId", ...)
                // .claim("email", ...)
                .build();

        // 3. Tạo JWS Header (Chỉ định thuật toán ký)
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // 4. Tạo Signed JWT
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        // 5. Ký token bằng Signer
        signedJWT.sign(signer);

        // 6. Serialize thành chuỗi JWT hoàn chỉnh
        String token = signedJWT.serialize();
        log.info("Generated JWT for user '{}'", authentication.getName());
        return token;
    }

    /**
     * Tạo JWT token dựa trên username và danh sách quyền.
     * Hữu ích nếu bạn không có đối tượng Authentication đầy đủ.
     *
     * @param username Tên người dùng.
     * @param authorities Danh sách quyền (không có tiền tố ROLE_).
     * @return Chuỗi JWT token đã được ký.
     * @throws JOSEException Nếu có lỗi trong quá trình tạo hoặc ký token.
     */
    public String generateToken(String username, List<String> authorities) throws JOSEException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        JWSSigner signer = new MACSigner(secretKey.getBytes());
        Instant now = Instant.now();
        Instant expirationTime = now.plus(expirationHours, ChronoUnit.HOURS);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer(issuer)
                .issueTime(Date.from(now))
                .expirationTime(Date.from(expirationTime))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", authorities != null ? authorities : List.of()) // Đảm bảo không null
                .build();

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        signedJWT.sign(signer);

        String token = signedJWT.serialize();
        log.info("Generated JWT for user '{}' (manual)", username);
        return token;
    }
}