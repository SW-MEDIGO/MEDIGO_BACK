package com.example.oswc.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupRequest {
    @NotBlank @Size(min = 4, max = 50)
    public String username;

    @NotBlank @Size(min = 8, max = 100)
    public String password;

    @NotBlank public String name;
    @NotBlank @Email public String email;

    public boolean termsOfService;
    public boolean privacyPolicy;
    public boolean locationService;
    public boolean ageLimit;
    public boolean marketingConsent;
}
