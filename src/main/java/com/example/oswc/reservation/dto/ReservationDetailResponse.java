package com.example.oswc.reservation.dto;

import com.example.oswc.reservation.ReservationStatus;
import java.util.Map;

public class ReservationDetailResponse {

  private String id;
  private String hospitalName;
  private String reservationDatetime;
  private String content;
  private ReservationStatus status;
  private Map<String, String> requestedManagerPreferences;
  private Map<String, String> paymentInfo;
  private Map<String, Object> user;
  private Map<String, Object> manager;

  // 생성자
  public ReservationDetailResponse() {}

  // Getters and Setters

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getHospitalName() {
    return hospitalName;
  }

  public void setHospitalName(String hospitalName) {
    this.hospitalName = hospitalName;
  }

  public String getReservationDatetime() {
    return reservationDatetime;
  }

  public void setReservationDatetime(String reservationDatetime) {
    this.reservationDatetime = reservationDatetime;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public ReservationStatus getStatus() {
    return status;
  }

  public void setStatus(ReservationStatus status) {
    this.status = status;
  }

  public Map<String, String> getRequestedManagerPreferences() {
    return requestedManagerPreferences;
  }

  public void setRequestedManagerPreferences(Map<String, String> requestedManagerPreferences) {
    this.requestedManagerPreferences = requestedManagerPreferences;
  }

  public Map<String, String> getPaymentInfo() {
    return paymentInfo;
  }

  public void setPaymentInfo(Map<String, String> paymentInfo) {
    this.paymentInfo = paymentInfo;
  }

  public Map<String, Object> getUser() {
    return user;
  }

  public void setUser(Map<String, Object> user) {
    this.user = user;
  }

  public Map<String, Object> getManager() {
    return manager;
  }

  public void setManager(Map<String, Object> manager) {
    this.manager = manager;
  }
}
