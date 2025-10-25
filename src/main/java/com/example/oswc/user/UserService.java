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

    /**
     * 회원가입 처리
     * - username / email 중복 체크
     * - 비밀번호 해시(BCrypt)
     * - 약관 동의 저장
     */
    public UserDocument signup(String username,
                               String rawPassword,
                               String name,
                               String email,
                               boolean tos, boolean privacy, boolean loc, boolean age, boolean mkt) {

        // 중복 검사
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

        var a = new Agreements(tos, privacy, loc, age, mkt, Instant.now().toEpochMilli());
        u.setAgreements(a);

        return userRepository.save(u);
    }

    // -------------------- Login (credential 검증) --------------------

    /**
     * 로그인 시 자격 증명 확인
     * - username 기준으로 사용자 조회 후, 비밀번호 매칭
     * - 실패 시 INVALID_CREDENTIALS
     */
    public UserDocument authenticate(String username, String rawPassword) {
        UserDocument u = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("INVALID_CREDENTIALS"));

        if (!passwordEncoder.matches(rawPassword, u.getPasswordHash())) {
            throw new IllegalArgumentException("INVALID_CREDENTIALS");
        }
        return u;
    }

    // -------------------- 조회/수정 (보호 구간에서 사용) --------------------

    /** ID로 사용자 조회 (없으면 USER_NOT_FOUND) */
    public UserDocument getById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));
    }

    /** 프로필 부분 수정 (이름, 마케팅 동의 예시) */
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

    // -------------------- 유틸 --------------------

    public Optional<UserDocument> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<UserDocument> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
