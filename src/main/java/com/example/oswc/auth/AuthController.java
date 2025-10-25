package com.example.oswc.auth;

import com.example.oswc.auth.dto.*;
import com.example.oswc.common.ApiResponse;
import com.example.oswc.user.UserDocument;
import com.example.oswc.user.UserRepository;
import com.example.oswc.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    public AuthController(UserService userService, JwtTokenProvider tokenProvider, UserRepository userRepository){
        this.userService=userService; this.tokenProvider=tokenProvider; this.userRepository=userRepository;
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest req){
        UserDocument u = userService.signup(req);
        return ResponseEntity.ok(ApiResponse.ok(Map.of(
            "id", u.getId(), "username", u.getUsername(), "email", u.getEmail(), "role", u.getRole()
        )));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req){
        UserDocument u = userService.authenticate(req.username, req.password);
        String at = tokenProvider.createAccessToken(u.getId(), u.getUsername(), u.getRole());
        String rt = tokenProvider.createRefreshToken(u.getId());
        return ResponseEntity.ok(ApiResponse.ok(new TokenResponse(at, rt)));
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Valid @RequestBody RefreshRequest req){
        var claims = tokenProvider.parseClaims(req.refreshToken);
        if(!"refresh".equals(claims.get("type"))) return ResponseEntity.status(401).body("INVALID_REFRESH_TOKEN");
        String userId = (String)claims.get("sub");
        UserDocument u = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("USER_NOT_FOUND"));
        String at = tokenProvider.createAccessToken(u.getId(), u.getUsername(), u.getRole());
        String rt = tokenProvider.createRefreshToken(u.getId());
        return ResponseEntity.ok(ApiResponse.ok(new TokenResponse(at, rt)));
    }
}
