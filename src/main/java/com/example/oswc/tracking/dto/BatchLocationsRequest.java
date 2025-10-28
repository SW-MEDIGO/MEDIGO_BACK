package com.example.oswc.tracking.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class BatchLocationsRequest {
  @Valid @NotNull private List<LocationDto> locations;

  public static class LocationDto {
    @NotNull private Double latitude;
    @NotNull private Double longitude;
    @NotNull private String timestamp; // ISO 8601

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

    public String getTimestamp() {
      return timestamp;
    }

    public void setTimestamp(String timestamp) {
      this.timestamp = timestamp;
    }
  }

  public List<LocationDto> getLocations() {
    return locations;
  }

  public void setLocations(List<LocationDto> locations) {
    this.locations = locations;
  }
}
