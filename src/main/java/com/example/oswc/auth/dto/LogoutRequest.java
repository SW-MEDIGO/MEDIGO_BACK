package com.example.oswc.auth.dto;

import jakarta.validation.constraints.NotBlank;

/** 로그아웃 요청 DTO */
public class LogoutRequest {

  @NotBlank(message = "refreshToken is required")
  private String refreshToken;

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
