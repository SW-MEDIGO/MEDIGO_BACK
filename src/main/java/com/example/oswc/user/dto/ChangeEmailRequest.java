package com.example.oswc.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangeEmailRequest {
    @NotBlank @Email
    private String newEmail;

    @NotBlank @Size(min = 8, max = 100)
    private String password; // 본인 확인용

    public String getNewEmail() { return newEmail; }
    public void setNewEmail(String newEmail) { this.newEmail = newEmail; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
