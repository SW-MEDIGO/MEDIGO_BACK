package com.example.oswc.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class UserDocument {
    @Id
    private String id;

    private String username;
    private String passwordHash;
    private String name;
    private String email;
    private String role; // USER / MANAGER

    private Agreements agreements;

    public static class Agreements {
        public boolean termsOfService;
        public boolean privacyPolicy;
        public boolean locationService;
        public boolean ageLimit;
        public boolean marketingConsent;
        public long   agreedAtEpochMillis;
    }

    public UserDocument() {}

    // ----- getters/setters -----
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
}
