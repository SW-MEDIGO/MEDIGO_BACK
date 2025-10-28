package com.example.oswc.facility;

import java.time.LocalTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

/** 시설 정보 (병원/약국) */
@Document(collection = "facilities")
public class FacilityDocument {

  @Id private String id;

  /** 시설명 */
  private String name;

  /** 시설 유형 */
  private FacilityType type;

  /** 위도 */
  private Double latitude;

  /** 경도 */
  private Double longitude;

  /** 2D 위치 (MongoDB GeoJSON) */
  @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
  private GeoJsonPoint location;

  /** 주소 */
  private String address;

  /** 상세 주소 */
  private String detailAddress;

  /** 전화번호 */
  private String phone;

  /** 운영 여부 */
  private Boolean isOperating;

  /** 운영 시작 시간 */
  private LocalTime openTime;

  /** 운영 종료 시간 */
  private LocalTime closeTime;

  /** 오늘 운영중인지 여부 */
  private Boolean openToday;

  // 기본 생성자
  public FacilityDocument() {}

  public FacilityDocument(
      String name,
      FacilityType type,
      Double latitude,
      Double longitude,
      String address,
      String detailAddress,
      String phone,
      Boolean isOperating,
      LocalTime openTime,
      LocalTime closeTime) {
    this.name = name;
    this.type = type;
    this.latitude = latitude;
    this.longitude = longitude;
    this.address = address;
    this.detailAddress = detailAddress;
    this.phone = phone;
    this.isOperating = isOperating;
    this.openTime = openTime;
    this.closeTime = closeTime;
    this.location = new GeoJsonPoint(longitude, latitude); // GeoJSON은 [경도, 위도] 순서
  }

  // Getters and Setters
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FacilityType getType() {
    return type;
  }

  public void setType(FacilityType type) {
    this.type = type;
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

  public GeoJsonPoint getLocation() {
    return location;
  }

  public void setLocation(GeoJsonPoint location) {
    this.location = location;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getDetailAddress() {
    return detailAddress;
  }

  public void setDetailAddress(String detailAddress) {
    this.detailAddress = detailAddress;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Boolean getIsOperating() {
    return isOperating;
  }

  public void setIsOperating(Boolean isOperating) {
    this.isOperating = isOperating;
  }

  public LocalTime getOpenTime() {
    return openTime;
  }

  public void setOpenTime(LocalTime openTime) {
    this.openTime = openTime;
  }

  public LocalTime getCloseTime() {
    return closeTime;
  }

  public void setCloseTime(LocalTime closeTime) {
    this.closeTime = closeTime;
  }

  public Boolean getOpenToday() {
    return openToday;
  }

  public void setOpenToday(Boolean openToday) {
    this.openToday = openToday;
  }
}
