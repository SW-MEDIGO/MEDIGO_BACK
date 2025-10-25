package com.example.oswc.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class UserDocument {

    @Id
    private String id;

    /** 로그인 아이디 (중복 금지) */
    @Indexed(unique = true)
    private String username;

    /** 비밀번호 해시(BCrypt 등) */
    private String passwordHash;

    /** 표시 이름 */
    private String name;

    /** 이메일 (중복 금지) */
    @Indexed(unique = true)
    private String email;

    /** 권한/역할: "USER" | "MANAGER" */
    private String role = "USER";

    /** 약관 동의 내장 문서 */
    private Agreements agreements;

    /** 생성 시각(에포크 밀리초) */
    private long createdAtEpochMillis;

    /**
     * 약관 동의 정보 (내장 문서)
     */
    public static class Agreements {
        public boolean termsOfService;
        public boolean privacyPolicy;
        public boolean locationService;
        public boolean ageLimit;
        public boolean marketingConsent;
        public long   agreedAtEpochMillis;

        public Agreements() { }

        public Agreements(boolean termsOfService,
                          boolean privacyPolicy,
                          boolean locationService,
                          boolean ageLimit,
                          boolean marketingConsent,
                          long agreedAtEpochMillis) {
            this.termsOfService = termsOfService;
            this.privacyPolicy = privacyPolicy;
            this.locationService = locationService;
            this.ageLimit = ageLimit;
            this.marketingConsent = marketingConsent;
            this.agreedAtEpochMillis = agreedAtEpochMillis;
        }
    }

    public UserDocument() { }

    // ------------------ Getters / Setters ------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Agreements getAgreements() {
        return agreements;
    }

    public void setAgreements(Agreements agreements) {
        this.agreements = agreements;
    }

    public long getCreatedAtEpochMillis() {
        return createdAtEpochMillis;
    }

    public void setCreatedAtEpochMillis(long createdAtEpochMillis) {
        this.createdAtEpochMillis = createdAtEpochMillis;
    }
}
