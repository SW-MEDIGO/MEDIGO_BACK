package com.example.oswc.user;

import com.example.oswc.user.UserDocument.Agreements;
import com.example.oswc.user.dto.UserProfilePatch;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // -------------------- Signup --------------------
    public UserDocument signup(String username,
                               String rawPassword,
                               String name,
                               String email,
                               boolean tos, boolean privacy, boolean loc, boolean age, boolean mkt) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("USERNAME_EXISTS");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("EMAIL_EXISTS");
        }

        var u = new UserDocument();
        u.setUsername(username);
        u.setPasswordHash(passwordEncoder.encode(rawPassword));
        u.setName(name);
        u.setEmail(email);
        u.setRole("USER");
        u.setCreatedAtEpochMillis(Instant.now().toEpochMilli());
        u.setAvatarUrl(null);

        var a = new Agreements(tos, privacy, loc, age, mkt, Instant.now().toEpochMilli());
        u.setAgreements(a);

        return userRepository.save(u);
    }

    // -------------------- Login --------------------
    public UserDocument authenticate(String username, String rawPassword) {
        UserDocument u = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("INVALID_CREDENTIALS"));
        if (!passwordEncoder.matches(rawPassword, u.getPasswordHash())) {
            throw new IllegalArgumentException("INVALID_CREDENTIALS");
        }
        return u;
    }

    // -------------------- 조회/수정 --------------------
    public UserDocument getById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));
    }

    public UserDocument updateProfile(String userId, UserProfilePatch patch) {
        var u = getById(userId);
        if (patch != null) {
            if (patch.name != null && !patch.name.isBlank()) {
                u.setName(patch.name.trim());
            }
            if (patch.marketingConsent != null) {
                var a = u.getAgreements();
                if (a != null) {
                    a.marketingConsent = patch.marketingConsent;
                    u.setAgreements(a);
                }
            }
        }
        return userRepository.save(u);
    }

    // -------------------- 이메일/비번/아바타 변경 --------------------
    public UserDocument changeEmail(String userId, String newEmail, String password) {
        var u = getById(userId);

        // 본인 확인: 현재 비밀번호 검사
        if (!passwordEncoder.matches(password, u.getPasswordHash())) {
            throw new IllegalArgumentException("INVALID_CREDENTIALS");
        }

        // 이메일 중복 검사 (다른 사람의 이메일과 중복 불가)
        var dup = userRepository.findByEmail(newEmail);
        if (dup.isPresent() && !dup.get().getId().equals(userId)) {
            throw new IllegalArgumentException("EMAIL_EXISTS");
        }

        u.setEmail(newEmail);
        return userRepository.save(u);
    }

    public void changePassword(String userId, String currentPassword, String newPassword) {
        var u = getById(userId);
        if (!passwordEncoder.matches(currentPassword, u.getPasswordHash())) {
            throw new IllegalArgumentException("INVALID_CREDENTIALS");
        }
        u.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(u);
    }

    public UserDocument setAvatarUrl(String userId, String avatarUrl) {
        var u = getById(userId);
        u.setAvatarUrl(avatarUrl);
        return userRepository.save(u);
    }

    // -------------------- 유틸 --------------------
    public Optional<UserDocument> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<UserDocument> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
