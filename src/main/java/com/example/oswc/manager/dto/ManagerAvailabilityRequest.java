package com.example.oswc.manager.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public class ManagerAvailabilityRequest {

  @Valid private ActivityArea activityArea; // optional

  @Valid private List<AvailableSlot> availableSchedule; // optional, [] 이면 전체 삭제

  public static class ActivityArea {
    private String address; // required when present

    @Min(0)
    private Integer radiusKm; // required when present

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }

    public Integer getRadiusKm() {
      return radiusKm;
    }

    public void setRadiusKm(Integer radiusKm) {
      this.radiusKm = radiusKm;
    }
  }

  public static class AvailableSlot {
    @Pattern(
        regexp = "(?i)MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY",
        message = "INVALID_INPUT")
    private String dayOfWeek;

    @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "INVALID_INPUT")
    private String startTime;

    @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "INVALID_INPUT")
    private String endTime;

    public String getDayOfWeek() {
      return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
      this.dayOfWeek = dayOfWeek;
    }

    public String getStartTime() {
      return startTime;
    }

    public void setStartTime(String startTime) {
      this.startTime = startTime;
    }

    public String getEndTime() {
      return endTime;
    }

    public void setEndTime(String endTime) {
      this.endTime = endTime;
    }
  }

  public ActivityArea getActivityArea() {
    return activityArea;
  }

  public void setActivityArea(ActivityArea activityArea) {
    this.activityArea = activityArea;
  }

  public List<AvailableSlot> getAvailableSchedule() {
    return availableSchedule;
  }

  public void setAvailableSchedule(List<AvailableSlot> availableSchedule) {
    this.availableSchedule = availableSchedule;
  }
}
