package com.example.oswc.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateReservationRequest {

  @NotBlank(message = "병원 이름은 필수입니다.")
  private String hospitalName;

  @NotBlank(message = "예약 날짜 및 시간은 필수입니다.")
  @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z$", message = "INVALID_INPUT")
  private String reservationDatetime;

  @NotBlank(message = "예약 내용은 필수입니다.")
  @Size(max = 500, message = "예약 내용은 500자 이하여야 합니다.")
  private String content;

  private String designatedManagerId; // 선택 사항

  private String managerGender; // "FEMALE" 또는 "MALE"

  @NotBlank(message = "결제 카드 ID는 필수입니다.")
  private String paymentCardId;

  // Getters and Setters

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

  public String getDesignatedManagerId() {
    return designatedManagerId;
  }

  public void setDesignatedManagerId(String designatedManagerId) {
    this.designatedManagerId = designatedManagerId;
  }

  public String getManagerGender() {
    return managerGender;
  }

  public void setManagerGender(String managerGender) {
    this.managerGender = managerGender;
  }

  public String getPaymentCardId() {
    return paymentCardId;
  }

  public void setPaymentCardId(String paymentCardId) {
    this.paymentCardId = paymentCardId;
  }
}
