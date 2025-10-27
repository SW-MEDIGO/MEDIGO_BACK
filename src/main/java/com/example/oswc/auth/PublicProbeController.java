package com.example.oswc.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 공개 체인 동작 확인용 핑 엔드포인트.
 * SecurityConfig의 publicChain(@Order(1))에서 /auth/** 를 permitAll로 설정해 두면
 * JWT 없이도 200 OK가 응답됩니다.
 */
@RestController
public class PublicProbeController {

    @GetMapping("/auth/ping")
    public Map<String, Object> ping() {
        return Map.of(
                "ok", true,
                "who", "public"
        );
    }
}
