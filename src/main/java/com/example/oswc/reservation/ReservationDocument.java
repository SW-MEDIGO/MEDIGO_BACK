package com.example.oswc.reservation;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reservations")
@CompoundIndexes({
  @CompoundIndex(name = "user_datetime_idx", def = "{ 'userId': 1, 'reservationDatetime': 1 }"),
  @CompoundIndex(
      name = "user_status_datetime_idx",
      def = "{ 'userId': 1, 'status': 1, 'reservationDatetime': 1 }")
})
public class ReservationDocument {

  @Id private String id;

  /** 예약을 생성한 사용자 ID */
  private String userId;

  /** 병원 이름 */
  private String hospitalName;

  /** 예약 날짜 및 시간 (UTC Instant) */
  private Instant reservationDatetime;

  /** 예약 내용 (진료과, 요청사항 등) */
  private String content;

  /** 예약 상태 */
  private ReservationStatus status;

  /** 지정된 매니저 ID (선택 사항) */
  private String designatedManagerId;

  /** 희망 매니저 성별 */
  private ManagerGender managerGender;

  /** 결제 카드 ID */
  private String paymentCardId;

  /** 할당된 매니저 ID */
  private String managerId;

  /** 매니저 이름 */
  private String managerName;

  /** 생성 시각 */
  private Long createdAtEpochMillis;

  /** 업데이트 시각 */
  private Long updatedAtEpochMillis;

  public ReservationDocument() {}

  // Getters and Setters

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getHospitalName() {
    return hospitalName;
  }

  public void setHospitalName(String hospitalName) {
    this.hospitalName = hospitalName;
  }

  public Instant getReservationDatetime() {
    return reservationDatetime;
  }

  public void setReservationDatetime(Instant reservationDatetime) {
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

  public String getDesignatedManagerId() {
    return designatedManagerId;
  }

  public void setDesignatedManagerId(String designatedManagerId) {
    this.designatedManagerId = designatedManagerId;
  }

  public ManagerGender getManagerGender() {
    return managerGender;
  }

  public void setManagerGender(ManagerGender managerGender) {
    this.managerGender = managerGender;
  }

  public String getPaymentCardId() {
    return paymentCardId;
  }

  public void setPaymentCardId(String paymentCardId) {
    this.paymentCardId = paymentCardId;
  }

  public String getManagerId() {
    return managerId;
  }

  public void setManagerId(String managerId) {
    this.managerId = managerId;
  }

  public String getManagerName() {
    return managerName;
  }

  public void setManagerName(String managerName) {
    this.managerName = managerName;
  }

  public Long getCreatedAtEpochMillis() {
    return createdAtEpochMillis;
  }

  public void setCreatedAtEpochMillis(Long createdAtEpochMillis) {
    this.createdAtEpochMillis = createdAtEpochMillis;
  }

  public Long getUpdatedAtEpochMillis() {
    return updatedAtEpochMillis;
  }

  public void setUpdatedAtEpochMillis(Long updatedAtEpochMillis) {
    this.updatedAtEpochMillis = updatedAtEpochMillis;
  }
}
