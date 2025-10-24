package com.example.oswc.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    public JwtAuthFilter(JwtTokenProvider tokenProvider){ this.tokenProvider = tokenProvider; }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String h = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (h!=null && h.startsWith("Bearer ")) {
            try{
                Map<String,Object> c = tokenProvider.parseClaims(h.substring(7));
                String sub = (String)c.get("sub"); String role=(String)c.get("role");
                if(sub!=null && role!=null){
                    var auth = new UsernamePasswordAuthenticationToken(
                        sub, null, List.of(new SimpleGrantedAuthority("ROLE_"+role)));
                    request.setAttribute("SPRING_SECURITY_AUTHENTICATION", auth);
                }
            }catch(Exception ignore){}
        }
        chain.doFilter(request,response);
    }
}
