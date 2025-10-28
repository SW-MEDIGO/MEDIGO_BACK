package com.example.oswc.reservation.dto;

import com.example.oswc.reservation.ManagerGender;
import com.example.oswc.reservation.ReservationStatus;

public class ReservationResponse {

  private String id;
  private String hospitalName;
  private String reservationDatetime;
  private String content;
  private ReservationStatus status;
  private ManagerGender managerGender;
  private String managerName;

  // 생성자
  public ReservationResponse() {}

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

  public ManagerGender getManagerGender() {
    return managerGender;
  }

  public void setManagerGender(ManagerGender managerGender) {
    this.managerGender = managerGender;
  }

  public String getManagerName() {
    return managerName;
  }

  public void setManagerName(String managerName) {
    this.managerName = managerName;
  }
}
