package com.example.oswc.auth;

import com.example.oswc.auth.dto.LoginRequest;
import com.example.oswc.auth.dto.LogoutRequest;
import com.example.oswc.auth.dto.RefreshRequest;
import com.example.oswc.auth.dto.SignupRequest;
import com.example.oswc.common.ApiResponse;
import com.example.oswc.user.UserDocument;
import com.example.oswc.user.UserService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final UserService userService;
  private final JwtTokenProvider tokenProvider;
  private final RefreshService refreshService;

  public AuthController(
      UserService userService, JwtTokenProvider tokenProvider, RefreshService refreshService) {
    this.userService = userService;
    this.tokenProvider = tokenProvider;
    this.refreshService = refreshService;
  }

  /** 회원가입 */
  @PostMapping("/signup")
  public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest req) {
    // UserService 시그니처(개별 파라미터)에 맞춰 전달
    UserDocument u =
        userService.signup(
            req.getUsername(),
            req.getPassword(),
            req.getName(),
            req.getEmail(),
            req.isTermsOfService(),
            req.isPrivacyPolicy(),
            req.isLocationService(),
            req.isAgeLimit(),
            req.isMarketingConsent());

    return ResponseEntity.ok(
        ApiResponse.ok(
            Map.of(
                "id", u.getId(),
                "username", u.getUsername(),
                "email", u.getEmail(),
                "name", u.getName(),
                "role", u.getRole())));
  }

  /** 로그인: AT/RT 발급 + RT 등록(로테이션 대비) */
  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
    UserDocument u = userService.authenticate(req.getUsername(), req.getPassword());

    String access = tokenProvider.createAccessToken(u.getId(), u.getUsername(), u.getRole());
    String refresh = tokenProvider.createRefreshToken(u.getId());

    // 로그인 직후 발급된 RT의 jti를 Redis에 등록
    refreshService.registerLoginRefresh(u, refresh);

    return ResponseEntity.ok(
        ApiResponse.ok(
            Map.of(
                "accessToken", access,
                "refreshToken", refresh,
                "tokenType", "Bearer")));
  }

  /** 리프레시: 제출된 RT가 유효(jti 존재)해야 새 AT/RT 발급 성공 시 옛 RT는 폐기되고 새 RT의 jti가 등록됨(로테이션). */
  @PostMapping("/refresh")
  public ResponseEntity<?> refresh(@Valid @RequestBody RefreshRequest req) {
    var pair = refreshService.rotate(req.getRefreshToken());
    return ResponseEntity.ok(
        ApiResponse.ok(
            Map.of(
                "accessToken", pair.accessToken(),
                "refreshToken", pair.refreshToken(),
                "tokenType", "Bearer")));
  }

  /**
   * 로그아웃: 제출된 RT의 jti를 즉시 제거 → 이후 재사용 불가
   *
   * @param req refreshToken을 포함하는 요청
   * @return 성공 메시지
   */
  @PostMapping("/logout")
  public ResponseEntity<?> logout(@Valid @RequestBody LogoutRequest req) {
    refreshService.logout(req.getRefreshToken());
    return ResponseEntity.ok(
        ApiResponse.ok(Map.of("message", "Successfully logged out", "loggedOut", true)));
  }
}
