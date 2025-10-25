package com.example.oswc.user.api;

import com.example.oswc.common.ApiResponse;
import com.example.oswc.user.UserDocument;
import com.example.oswc.user.UserService;
import com.example.oswc.user.dto.UserProfilePatch;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;
    public UsersController(UserService userService){ this.userService = userService; }

    // 로그인 사용자 정보
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth){
        UserDocument u = userService.getById(auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(Map.of(
                "id", u.getId(),
                "username", u.getUsername(),
                "name", u.getName(),
                "email", u.getEmail(),
                "role", u.getRole(),
                "agreements", u.getAgreements(),
                "createdAt", u.getCreatedAtEpochMillis()
        )));
    }

    // 부분 수정
    @PatchMapping("/me")
    public ResponseEntity<?> patch(Authentication auth, @RequestBody UserProfilePatch patch){
        UserDocument saved = userService.updateProfile(auth.getName(), patch);
        return ResponseEntity.ok(ApiResponse.ok(Map.of(
                "id", saved.getId(),
                "name", saved.getName(),
                "agreements", saved.getAgreements()
        )));
    }
}
