package com.example.oswc.manager;

import com.example.oswc.common.ApiResponse;
import com.example.oswc.manager.dto.ManagerAvailabilityRequest;
import com.example.oswc.manager.dto.ManagerAvailabilityResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ManagerAvailabilityService {

  private final ManagerAvailabilityRepository repository;

  public ManagerAvailabilityService(ManagerAvailabilityRepository repository) {
    this.repository = repository;
  }

  private void ensureManagerRole(String role) {
    if (role == null || !role.equalsIgnoreCase("MANAGER")) {
      throw new RuntimeException("매니저 계정으로만 접근 가능합니다.");
    }
  }

  public ApiResponse<?> getMine(String managerId, String role) {
    ensureManagerRole(role);
    var doc = repository.findByManagerId(managerId).orElse(null);
    var resp = new ManagerAvailabilityResponse();
    if (doc != null) {
      if (doc.getActivityArea() != null) {
        var aa = new ManagerAvailabilityResponse.ActivityArea();
        aa.setAddress(doc.getActivityArea().getAddress());
        aa.setRadiusKm(doc.getActivityArea().getRadiusKm());
        resp.setActivityArea(aa);
      }
      if (doc.getAvailableSchedule() != null) {
        List<ManagerAvailabilityResponse.AvailableSlot> slots =
            doc.getAvailableSchedule().stream()
                .map(
                    s -> {
                      var r = new ManagerAvailabilityResponse.AvailableSlot();
                      r.setDayOfWeek(s.getDayOfWeek());
                      r.setStartTime(s.getStartTime());
                      r.setEndTime(s.getEndTime());
                      return r;
                    })
                .collect(Collectors.toList());
        resp.setAvailableSchedule(slots);
      }
    }
    return ApiResponse.ok(
        java.util.Map.of(
            "status",
            "success",
            "data",
            java.util.Map.of(
                "activity_area",
                resp.getActivityArea(),
                "available_schedule",
                resp.getAvailableSchedule())));
  }

  public ApiResponse<?> patchMine(String managerId, String role, ManagerAvailabilityRequest req) {
    ensureManagerRole(role);
    var doc = repository.findByManagerId(managerId).orElseGet(ManagerAvailabilityDocument::new);
    doc.setId(managerId);
    doc.setManagerId(managerId);

    if (req.getActivityArea() != null) {
      var aa = new ManagerAvailabilityDocument.ActivityArea();
      aa.setAddress(req.getActivityArea().getAddress());
      aa.setRadiusKm(req.getActivityArea().getRadiusKm());
      doc.setActivityArea(aa);
    }
    if (req.getAvailableSchedule() != null) {
      List<ManagerAvailabilityDocument.AvailableSlot> slots =
          req.getAvailableSchedule().stream()
              .map(
                  s -> {
                    var r = new ManagerAvailabilityDocument.AvailableSlot();
                    r.setDayOfWeek(s.getDayOfWeek());
                    r.setStartTime(s.getStartTime());
                    r.setEndTime(s.getEndTime());
                    return r;
                  })
              .collect(Collectors.toList());
      doc.setAvailableSchedule(slots);
    }

    repository.save(doc);

    return ApiResponse.ok(java.util.Map.of("message", "활동 가능 정보가 성공적으로 업데이트되었습니다."));
  }
}
