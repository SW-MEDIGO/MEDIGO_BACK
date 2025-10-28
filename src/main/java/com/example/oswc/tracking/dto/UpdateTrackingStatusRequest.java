package com.example.oswc.tracking.dto;

import jakarta.validation.constraints.Pattern;

public class UpdateTrackingStatusRequest {

  @Pattern(
      regexp = "(?i)SERVICE_STARTED|ARRIVED_AT_HOSPITAL|SERVICE_COMPLETED",
      message = "INVALID_INPUT")
  private String status;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
