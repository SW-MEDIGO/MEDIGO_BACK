package com.example.oswc.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class RefreshRequest {
    @NotBlank public String refreshToken;
}
