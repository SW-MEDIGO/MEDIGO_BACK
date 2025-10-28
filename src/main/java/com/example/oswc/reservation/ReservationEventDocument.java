package com.example.oswc.reservation;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reservation_events")
public class ReservationEventDocument {

  @Id private String id;

  private String reservationId;
  private String userId;
  private ReservationEventType eventType;
  private String message;
  private Instant occurredAt;
  private String actor; // USER, MANAGER, ADMIN

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

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

  public ReservationEventType getEventType() {
    return eventType;
  }

  public void setEventType(ReservationEventType eventType) {
    this.eventType = eventType;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Instant getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(Instant occurredAt) {
    this.occurredAt = occurredAt;
  }

  public String getActor() {
    return actor;
  }

  public void setActor(String actor) {
    this.actor = actor;
  }
}
