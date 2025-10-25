package com.example.oswc.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.access-exp-min}")
    private long accessExpMin;

    @Value("${app.jwt.refresh-exp-days}")
    private long refreshExpDays;

    private SecretKey key; // ✅ SecretKey 로 변경

    @PostConstruct
    void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(String userId, String username, String role) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(accessExpMin * 60);
        return Jwts.builder()
                .subject(userId)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(String userId) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(refreshExpDays * 24 * 60 * 60);
        return Jwts.builder()
                .subject(userId)
                .claim("type", "refresh")
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> parseClaims(String token) {
        var claimsJws = Jwts.parser()
                .verifyWith(key) // ✅ SecretKey 사용
                .build()
                .parseSignedClaims(token);
        return claimsJws.getPayload();
    }
}
