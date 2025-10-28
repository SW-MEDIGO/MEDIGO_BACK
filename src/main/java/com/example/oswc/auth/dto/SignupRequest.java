package com.example.oswc.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupRequest {

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    // 약관 동의들
    private boolean termsOfService;
    private boolean privacyPolicy;
    private boolean locationService;
    private boolean ageLimit;
    private boolean marketingConsent;

    // ---- getters / setters ----
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isTermsOfService() { return termsOfService; }
    public void setTermsOfService(boolean termsOfService) { this.termsOfService = termsOfService; }

    public boolean isPrivacyPolicy() { return privacyPolicy; }
    public void setPrivacyPolicy(boolean privacyPolicy) { this.privacyPolicy = privacyPolicy; }

    public boolean isLocationService() { return locationService; }
    public void setLocationService(boolean locationService) { this.locationService = locationService; }

    public boolean isAgeLimit() { return ageLimit; }
    public void setAgeLimit(boolean ageLimit) { this.ageLimit = ageLimit; }

    public boolean isMarketingConsent() { return marketingConsent; }
    public void setMarketingConsent(boolean marketingConsent) { this.marketingConsent = marketingConsent; }
}
