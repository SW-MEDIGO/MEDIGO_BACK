package com.example.oswc.tracking;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reservation_tracking")
public class TrackingDocument {

  @Id private String reservationId;

  private String userId;
  private String managerId;

  private TrackingStatus status;

  private Double lastLatitude;
  private Double lastLongitude;
  private Instant lastUpdatedAt;

  public String getReservationId() {
    return reservationId;
  }

  public void setReservationId(String reservationId) {
    this.reservationId = reservationId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getManagerId() {
    return managerId;
  }

  public void setManagerId(String managerId) {
    this.managerId = managerId;
  }

  public TrackingStatus getStatus() {
    return status;
  }

  public void setStatus(TrackingStatus status) {
    this.status = status;
  }

  public Double getLastLatitude() {
    return lastLatitude;
  }

  public void setLastLatitude(Double lastLatitude) {
    this.lastLatitude = lastLatitude;
  }

  public Double getLastLongitude() {
    return lastLongitude;
  }

  public void setLastLongitude(Double lastLongitude) {
    this.lastLongitude = lastLongitude;
  }

  public Instant getLastUpdatedAt() {
    return lastUpdatedAt;
  }

  public void setLastUpdatedAt(Instant lastUpdatedAt) {
    this.lastUpdatedAt = lastUpdatedAt;
  }
}
