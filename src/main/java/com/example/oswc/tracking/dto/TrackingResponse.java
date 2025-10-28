package com.example.oswc.tracking.dto;

public class TrackingResponse {
  private String reservationId;
  private String trackingStatus;
  private Double latitude;
  private Double longitude;
  private String updatedAt;
  private String managerName;

  public String getReservationId() {
    return reservationId;
  }

  public void setReservationId(String reservationId) {
    this.reservationId = reservationId;
  }

  public String getTrackingStatus() {
    return trackingStatus;
  }

  public void setTrackingStatus(String trackingStatus) {
    this.trackingStatus = trackingStatus;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getManagerName() {
    return managerName;
  }

  public void setManagerName(String managerName) {
    this.managerName = managerName;
  }
}
