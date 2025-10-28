package com.example.oswc.manager.dto;

import java.util.List;

public class ManagerAvailabilityResponse {
  private ActivityArea activityArea;
  private List<AvailableSlot> availableSchedule;

  public static class ActivityArea {
    private String address;
    private Integer radiusKm;

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
    private String dayOfWeek;
    private String startTime;
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
