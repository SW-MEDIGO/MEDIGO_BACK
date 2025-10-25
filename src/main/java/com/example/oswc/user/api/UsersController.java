package com.example.oswc.user.api;

import com.example.oswc.common.ApiResponse;
import com.example.oswc.common.FileStorageService;
import com.example.oswc.user.UserDocument;
import com.example.oswc.user.UserService;
import com.example.oswc.user.dto.ChangeEmailRequest;
import com.example.oswc.user.dto.ChangePasswordRequest;
import com.example.oswc.user.dto.UserProfilePatch;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;
    private final FileStorageService storageService;

    public UsersController(UserService userService, FileStorageService storageService){
        this.userService = userService;
        this.storageService = storageService;
    }

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
                "createdAt", u.getCreatedAtEpochMillis(),
                "avatarUrl", u.getAvatarUrl()
        )));
    }

    // 프로필 부분 수정(이름/마케팅 동의 등)
    @PatchMapping("/me")
    public ResponseEntity<?> patch(Authentication auth, @RequestBody UserProfilePatch patch){
        UserDocument saved = userService.updateProfile(auth.getName(), patch);
        return ResponseEntity.ok(ApiResponse.ok(Map.of(
                "id", saved.getId(),
                "name", saved.getName(),
                "agreements", saved.getAgreements(),
                "avatarUrl", saved.getAvatarUrl()
        )));
    }

    // ① 이메일 변경 (현재 비번 확인 포함)
    @PatchMapping("/me/email")
    public ResponseEntity<?> changeEmail(Authentication auth, @Valid @RequestBody ChangeEmailRequest req){
        UserDocument saved = userService.changeEmail(auth.getName(), req.getNewEmail(), req.getPassword());
        return ResponseEntity.ok(ApiResponse.ok(Map.of("email", saved.getEmail())));
    }

    // ② 비밀번호 변경 (현재 비번 확인 → 새 비번 저장)
    @PatchMapping("/me/password")
    public ResponseEntity<?> changePassword(Authentication auth, @Valid @RequestBody ChangePasswordRequest req){
        userService.changePassword(auth.getName(), req.getCurrentPassword(), req.getNewPassword());
        return ResponseEntity.ok(ApiResponse.ok(Map.of("changed", true)));
    }

    // ③ 아바타 업로드 (multipart/form-data, field: file)
    @PostMapping("/me/avatar")
    public ResponseEntity<?> uploadAvatar(Authentication auth, @RequestParam("file") MultipartFile file) throws Exception {
        String url = storageService.saveUserAvatar(auth.getName(), file);
        UserDocument saved = userService.setAvatarUrl(auth.getName(), url);
        return ResponseEntity.ok(ApiResponse.ok(Map.of("avatarUrl", saved.getAvatarUrl())));
    }
}
