package com.example.oswc.facility.dto;

import com.example.oswc.facility.FacilityType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** 시설 검색 요청 DTO (명세서 기준) */
public class FacilitySearchRequest {

  /** 위도 (필수) */
  @NotNull(message = "latitude is required")
  @Min(value = -90, message = "latitude must be between -90 and 90")
  @Max(value = 90, message = "latitude must be between -90 and 90")
  private Double latitude;

  /** 경도 (필수) */
  @NotNull(message = "longitude is required")
  @Min(value = -180, message = "longitude must be between -180 and 180")
  @Max(value = 180, message = "longitude must be between -180 and 180")
  private Double longitude;

  /** 시설 유형 (기본값: HOSPITAL) */
  private FacilityType type = FacilityType.HOSPITAL;

  /** 현재 운영중인 곳만 필터링 */
  private Boolean openNow = false;

  /** 반경 (미터, 기본값: 3000m = 3km) */
  @Min(value = 100, message = "radius must be at least 100 meters")
  @Max(value = 50000, message = "radius must be at most 50000 meters")
  private Integer radius = 3000;

  /** 페이지 번호 (1부터 시작, 기본값: 1) */
  @Min(value = 1, message = "page must be >= 1")
  private Integer page = 1;

  /** 한 페이지에 보여줄 항목 수 (기본값: 10) */
  @Min(value = 1, message = "limit must be >= 1")
  @Max(value = 100, message = "limit must be <= 100")
  private Integer limit = 10;

  // Getters and Setters
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

  public FacilityType getType() {
    return type;
  }

  public void setType(FacilityType type) {
    this.type = type;
  }

  public Boolean getOpenNow() {
    return openNow;
  }

  public void setOpenNow(Boolean openNow) {
    this.openNow = openNow;
  }

  public Integer getRadius() {
    return radius;
  }

  public void setRadius(Integer radius) {
    this.radius = radius;
  }

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public Integer getLimit() {
    return limit;
  }

  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  // Spring Pageable을 위한 변환 메서드 (page 1-based → 0-based)
  public Integer getPageForPagination() {
    return page - 1;
  }
}
