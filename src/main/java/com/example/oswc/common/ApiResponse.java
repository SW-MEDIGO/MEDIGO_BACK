package com.example.oswc.common;

public record ApiResponse<T>(String status, T data) {
  public static <T> ApiResponse<T> ok(T data) {
    return new ApiResponse<>("success", data);
  }
}
