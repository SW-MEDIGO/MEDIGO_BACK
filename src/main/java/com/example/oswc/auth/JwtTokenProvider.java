package com.example.oswc.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey; // ★ 핵심: SecretKey 사용

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.access-exp-min:30}")
    private long accessExpMin;

    @Value("${app.jwt.refresh-exp-days:14}")
    private long refreshExpDays;

    private SecretKey key; // ★ Key → SecretKey

    @PostConstruct
    void init() {
        // HS256용 대칭키 생성 (최소 32바이트 권장)
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException("app.jwt.secret must be >= 32 bytes");
        }
        this.key = Keys.hmacShaKeyFor(bytes); // 반환 타입이 SecretKey
    }

    /** 액세스 토큰 생성 (type=access) */
    public String createAccessToken(String userId, String username, String role) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(accessExpMin * 60);

        return Jwts.builder()
                .subject(userId)
                .claim("username", username)
                .claim("role", role)
                .claim("type", "access")
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** 리프레시 토큰 생성 (type=refresh, jti 포함) */
    public String createRefreshToken(String userId) {
        String jti = UUID.randomUUID().toString();
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(refreshExpDays * 24 * 60 * 60);

        return Jwts.builder()
                .subject(userId)
                .claim("type", "refresh")
                .id(jti) // jti
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** 토큰에서 클레임 파싱(검증 포함) */
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)   // ★ 이제 타입이 SecretKey라 컴파일 OK
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
