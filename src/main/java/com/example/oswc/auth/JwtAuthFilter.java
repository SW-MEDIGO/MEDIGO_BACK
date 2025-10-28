package com.example.oswc.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/** 
 * Authorization 헤더가 있을 때만 인증 시도.
 * 공개 경로(/auth/**, /health, /swagger-ui/**, /v3/api-docs/**, /files/**)와
 * 모든 OPTIONS 요청은 필터 자체를 건너뜀.
 * 실패해도 여기서 401을 리턴하지 않고 다음 필터로 넘김.
 */
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtAuthFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 컨텍스트 경로 고려 없이 서블릿 경로만 사용 (스프링 추천)
        String path = request.getServletPath();

        // 공개 경로는 필터 스킵
        if (pathMatcher.match("/auth/**", path)) return true;
        if (pathMatcher.match("/health", path)) return true;
        if (path.equals("/swagger-ui.html")) return true;
        if (pathMatcher.match("/swagger-ui/**", path)) return true;
        if (pathMatcher.match("/v3/api-docs/**", path)) return true;
        if (pathMatcher.match("/files/**", path)) return true;
        if (pathMatcher.match("/facilities/**", path)) return true;

        // CORS 프리플라이트는 무조건 스킵
        if (HttpMethod.OPTIONS.matches(request.getMethod())) return true;

        // Authorization 헤더 자체가 없으면 스킵 (permitAll 고려)
        String auth = request.getHeader("Authorization");
        return (auth == null || auth.isBlank());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String auth = request.getHeader("Authorization");
        String token = auth.substring("Bearer ".length()).trim();

        try {
            Claims claims = tokenProvider.parseClaims(token);

            String userId = claims.getSubject();
            String role = claims.get("role", String.class);

            List<GrantedAuthority> authorities =
                    (role != null && !role.isBlank())
                            ? List.of(new SimpleGrantedAuthority("ROLE_" + role))
                            : Collections.emptyList();

            var authentication = new UsernamePasswordAuthenticationToken(
                    userId, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            // 여기서 401을 직접 내리지 않음
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
