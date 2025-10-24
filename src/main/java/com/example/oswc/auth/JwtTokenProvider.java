package com.example.oswc.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt.secret}") private String secret;
    @Value("${app.jwt.access-exp-min}") private long accessExpMin;
    @Value("${app.jwt.refresh-exp-days}") private long refreshExpDays;
    private Key key;

    @PostConstruct void init(){ key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); }

    public String createAccessToken(String userId, String username, String role){
        Instant now=Instant.now(), exp=now.plusSeconds(accessExpMin*60);
        return Jwts.builder().subject(userId)
                .claim("username", username).claim("role", role)
                .issuedAt(Date.from(now)).expiration(Date.from(exp))
                .signWith(key).compact();
    }
    public String createRefreshToken(String userId){
        Instant now=Instant.now(), exp=now.plusSeconds(refreshExpDays*24*60*60);
        return Jwts.builder().subject(userId).claim("type","refresh")
                .issuedAt(Date.from(now)).expiration(Date.from(exp))
                .signWith(key).compact();
    }
    public Map<String,Object> parseClaims(String token){
        var claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return claims;
    }
}
