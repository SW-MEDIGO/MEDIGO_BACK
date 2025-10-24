package com.example.oswc.config;

import com.example.oswc.auth.JwtAuthFilter;
import com.example.oswc.auth.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtTokenProvider tokenProvider) throws Exception {
        http.csrf(csrf->csrf.disable())
           .cors(Customizer.withDefaults())
           .securityContext(ctx -> ctx.securityContextRepository(
               new RequestAttributeSecurityContextRepository(){
                   @Override
                   public void saveContext(org.springframework.security.core.context.SecurityContext context,
                                           HttpServletRequest request,
                                           jakarta.servlet.http.HttpServletResponse response){
                       Object a = request.getAttribute("SPRING_SECURITY_AUTHENTICATION");
                       if (a instanceof Authentication auth) context.setAuthentication(auth);
                   }
               })
           )
           .authorizeHttpRequests(auth -> auth
               .requestMatchers("/health","/swagger-ui/**","/v3/api-docs/**").permitAll()
               .requestMatchers(HttpMethod.POST,"/auth/signup","/auth/login","/auth/refresh").permitAll()
               .anyRequest().authenticated()
           )
           .addFilterBefore(new JwtAuthFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean public BCryptPasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }
}
