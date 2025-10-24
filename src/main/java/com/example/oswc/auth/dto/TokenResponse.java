package com.example.oswc.auth.dto;

public class TokenResponse {
    public String accessToken;
    public String refreshToken;
    public String tokenType = "Bearer";
    public TokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken; this.refreshToken = refreshToken;
    }
}
