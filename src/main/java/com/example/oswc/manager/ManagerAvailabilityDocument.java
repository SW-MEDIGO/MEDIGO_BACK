package com.example.oswc.manager;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "manager_availability")
public class ManagerAvailabilityDocument {

  @Id private String id; // managerId 와 동일하게 저장

  private String managerId;

  private ActivityArea activityArea; // 주소 + 반경(km)

  private List<AvailableSlot> availableSchedule; // 요일/시간 목록

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
    private String dayOfWeek; // MONDAY ... SUNDAY
    private String startTime; // HH:MM
    private String endTime; // HH:MM

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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getManagerId() {
    return managerId;
  }

  public void setManagerId(String managerId) {
    this.managerId = managerId;
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
