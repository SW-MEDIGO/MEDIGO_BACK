package com.example.oswc.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class UserDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    private String passwordHash;

    private String name;

    @Indexed(unique = true)
    private String email;

    private String role; // USER / MANAGER

    private Agreements agreements;

    /** 계정 생성 시각 (epoch millis) */
    private Long createdAtEpochMillis;

    /** 프로필 이미지(아바타) 접근 URL (예: /files/avatars/<userId>.png) */
    private String avatarUrl;

    // ---------- Inner Types ----------

    public static class Agreements {
        public boolean termsOfService;
        public boolean privacyPolicy;
        public boolean locationService;
        public boolean ageLimit;
        public boolean marketingConsent;
        public long   agreedAtEpochMillis;

        public Agreements() { }

        public Agreements(boolean tos, boolean priv, boolean loc, boolean age, boolean mkt, long at) {
            this.termsOfService = tos;
            this.privacyPolicy = priv;
            this.locationService = loc;
            this.ageLimit = age;
            this.marketingConsent = mkt;
            this.agreedAtEpochMillis = at;
        }
    }

    // ---------- Constructors ----------

    public UserDocument() { }

    // ---------- Getters / Setters ----------

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Agreements getAgreements() { return agreements; }
    public void setAgreements(Agreements agreements) { this.agreements = agreements; }

    public Long getCreatedAtEpochMillis() { return createdAtEpochMillis; }
    public void setCreatedAtEpochMillis(Long createdAtEpochMillis) { this.createdAtEpochMillis = createdAtEpochMillis; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}
