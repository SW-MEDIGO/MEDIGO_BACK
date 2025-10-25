package com.example.oswc.auth;

import com.example.oswc.common.ApiResponse;
import com.example.oswc.user.UserDocument;
import com.example.oswc.user.UserService;
import com.example.oswc.auth.dto.LoginRequest;
import com.example.oswc.auth.dto.SignupRequest;
import com.example.oswc.auth.dto.RefreshRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    public AuthController(UserService userService, JwtTokenProvider tokenProvider) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest req) {
        // UserService 시그니처(개별 파라미터)에 맞춰 전달
        UserDocument u = userService.signup(
                req.getUsername(),
                req.getPassword(),
                req.getName(),
                req.getEmail(),
                req.isTermsOfService(),
                req.isPrivacyPolicy(),
                req.isLocationService(),
                req.isAgeLimit(),
                req.isMarketingConsent()
        );

        return ResponseEntity.ok(ApiResponse.ok(Map.of(
                "id", u.getId(),
                "username", u.getUsername(),
                "email", u.getEmail(),
                "name", u.getName(),
                "role", u.getRole()
        )));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        UserDocument u = userService.authenticate(req.getUsername(), req.getPassword());

        String access = tokenProvider.createAccessToken(u.getId(), u.getUsername(), u.getRole());
        String refresh = tokenProvider.createRefreshToken(u.getId());

        return ResponseEntity.ok(ApiResponse.ok(Map.of(
                "accessToken", access,
                "refreshToken", refresh,
                "tokenType", "Bearer"
        )));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Valid @RequestBody RefreshRequest req) {
        // 단순 버전: refresh 토큰 검증 후 새 토큰 쌍 발급
        var claims = tokenProvider.parseClaims(req.getRefreshToken());
        if (!"refresh".equals(claims.get("type"))) {
            throw new IllegalArgumentException("INVALID_REFRESH_TOKEN");
        }
        String userId = (String) claims.get("sub");
        UserDocument u = userService.getById(userId);

        String access = tokenProvider.createAccessToken(u.getId(), u.getUsername(), u.getRole());
        String refresh = tokenProvider.createRefreshToken(u.getId());

        return ResponseEntity.ok(ApiResponse.ok(Map.of(
                "accessToken", access,
                "refreshToken", refresh,
                "tokenType", "Bearer"
        )));
    }
}
