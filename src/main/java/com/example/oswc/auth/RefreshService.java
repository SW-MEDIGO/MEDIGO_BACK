package com.example.oswc.auth;

import com.example.oswc.user.UserDocument;
import com.example.oswc.user.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class RefreshService {

  private final JwtTokenProvider tokenProvider;
  private final RefreshTokenStore store;
  private final UserService userService;

  public RefreshService(
      JwtTokenProvider tokenProvider, RefreshTokenStore store, UserService userService) {
    this.tokenProvider = tokenProvider;
    this.store = store;
    this.userService = userService;
  }

  /** 로그인 직후: 새 RT 의 jti를 Redis에 저장 */
  public void registerLoginRefresh(UserDocument u, String refreshToken) {
    Claims c = tokenProvider.parseClaims(refreshToken);
    if (!"refresh".equals(c.get("type")))
      throw new IllegalArgumentException("INVALID_REFRESH_TOKEN");
    store.store(u.getId(), c.getId());
  }

  /** 재발급: 제출된 RT의 jti가 살아있어야 유효. 성공 시 로테이션. */
  public TokenPair rotate(String presentedRefreshToken) {
    Claims c = tokenProvider.parseClaims(presentedRefreshToken);
    if (!"refresh".equals(c.get("type")))
      throw new IllegalArgumentException("INVALID_REFRESH_TOKEN");
    String userId = c.getSubject();
    String jti = c.getId();

    if (!store.exists(userId, jti)) {
      throw new IllegalArgumentException("REUSED_REFRESH_TOKEN"); // 또는 INVALID_REFRESH_TOKEN
    }

    // 사용자 확인
    UserDocument u = userService.getById(userId);

    // 새 토큰 발급
    String newAccess = tokenProvider.createAccessToken(u.getId(), u.getUsername(), u.getRole());
    String newRefresh = tokenProvider.createRefreshToken(u.getId());

    // 로테이션: 옛 jti 삭제 → 새 jti 저장
    store.remove(userId, jti);
    Claims newC = tokenProvider.parseClaims(newRefresh);
    store.store(userId, newC.getId());

    return new TokenPair(newAccess, newRefresh);
  }

  /** 로그아웃: 제출된 RT jti 제거 - 토큰이 유효하지 않거나 타입이 맞지 않으면 예외 발생 - 이미 제거된 토큰이라도 문제없음 (idempotent) */
  public void logout(String presentedRefreshToken) {
    try {
      Claims c = tokenProvider.parseClaims(presentedRefreshToken);
      if (!"refresh".equals(c.get("type"))) {
        throw new IllegalArgumentException("INVALID_REFRESH_TOKEN");
      }
      String userId = c.getSubject();
      String jti = c.getId();

      // Redis에서 해당 jti 제거 (이미 없어도 무방)
      store.remove(userId, jti);
    } catch (Exception e) {
      // 토큰 파싱 실패(만료, 변조 등) 시 INVALID_REFRESH_TOKEN으로 처리
      if (e instanceof IllegalArgumentException) {
        throw e; // 우리가 던진 예외는 그대로 전달
      }
      throw new IllegalArgumentException("INVALID_REFRESH_TOKEN", e);
    }
  }

  public record TokenPair(String accessToken, String refreshToken) {}
}
