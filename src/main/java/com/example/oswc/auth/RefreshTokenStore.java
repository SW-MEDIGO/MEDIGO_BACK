package com.example.oswc.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RefreshTokenStore {

    private final StringRedisTemplate redis;
    private final Duration ttl;

    public RefreshTokenStore(StringRedisTemplate redis,
                             @Value("${app.jwt.refresh-exp-days:14}") long days) {
        this.redis = redis;
        this.ttl = Duration.ofDays(days);
    }

    private String key(String userId, String jti) {
        return "rt:" + userId + ":" + jti;
    }

    /** 등록(로테이션 시 신규 jti 저장) */
    public void store(String userId, String jti) {
        redis.opsForValue().set(key(userId, jti), "1", ttl);
    }

    /** 존재 확인(재발급 시 제출된 jti가 살아있는지) */
    public boolean exists(String userId, String jti) {
        Boolean has = redis.hasKey(key(userId, jti));
        return has != null && has;
    }

    /** 제거(로테이션/로그아웃/유출 대응) */
    public void remove(String userId, String jti) {
        redis.delete(key(userId, jti));
    }
}
