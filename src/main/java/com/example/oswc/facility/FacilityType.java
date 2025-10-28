package com.example.oswc.facility;

/** 시설 유형 */
public enum FacilityType {
  HOSPITAL("병원"),
  PHARMACY("약국");

  private final String description;

  FacilityType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
