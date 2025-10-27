package com.example.oswc.facility.dto;

import com.example.oswc.facility.FacilityType;

/**
 * 시설 응답 DTO (명세서 기준)
 */
public class FacilityResponse {

  private String id;
  private String name;
  private FacilityType type;
  private String address;
  private String phone;
  
  /** 위치 정보 (nested object) */
  private LocationInfo location;
  
  /** 거리 (미터 단위) */
  private Integer distanceMeters;
  
  /** 현재 운영중인지 여부 */
  private Boolean isOpen;
  
  /** 오늘의 운영시간 */
  private String operatingHoursToday;

  // 기본 생성자
  public FacilityResponse() {}

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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public LocationInfo getLocation() {
    return location;
  }

  public void setLocation(LocationInfo location) {
    this.location = location;
  }

  public Integer getDistanceMeters() {
    return distanceMeters;
  }

  public void setDistanceMeters(Integer distanceMeters) {
    this.distanceMeters = distanceMeters;
  }

  public Boolean getIsOpen() {
    return isOpen;
  }

  public void setIsOpen(Boolean isOpen) {
    this.isOpen = isOpen;
  }

  public String getOperatingHoursToday() {
    return operatingHoursToday;
  }

  public void setOperatingHoursToday(String operatingHoursToday) {
    this.operatingHoursToday = operatingHoursToday;
  }

  /**
   * 위치 정보 내부 클래스
   */
  public static class LocationInfo {
    private Double latitude;
    private Double longitude;

    public LocationInfo() {}

    public LocationInfo(Double latitude, Double longitude) {
      this.latitude = latitude;
      this.longitude = longitude;
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
  }
}
