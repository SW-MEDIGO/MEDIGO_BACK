package com.example.oswc.tracking.api;

import com.example.oswc.common.ApiResponse;
import com.example.oswc.tracking.TrackingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tracking")
public class TrackingController {

  private final TrackingService trackingService;

  public TrackingController(TrackingService trackingService) {
    this.trackingService = trackingService;
  }

  /** GET /tracking/reservations/{id} - 실시간 동행 상태 조회 */
  @GetMapping("/reservations/{id}")
  public ResponseEntity<?> getTracking(Authentication auth, @PathVariable String id) {
    String userId = auth.getName();
    ApiResponse<?> resp = trackingService.getTracking(id, userId);
    return ResponseEntity.ok(resp);
  }

  /** PATCH /tracking/reservations/{id}/status - 동행 상태 업데이트 (매니저 전용) */
  @PatchMapping("/reservations/{id}/status")
  public ResponseEntity<?> updateStatus(
      Authentication auth,
      @PathVariable String id,
      @jakarta.validation.Valid @org.springframework.web.bind.annotation.RequestBody
          com.example.oswc.tracking.dto.UpdateTrackingStatusRequest req) {
    String managerId = auth.getName();
    var resp = trackingService.updateStatus(id, managerId, req.getStatus().toUpperCase());
    return ResponseEntity.ok(resp);
  }

  /** PATCH /tracking/reservations/{id}/location - 위치 배치 업데이트 (매니저 전용) */
  @PatchMapping("/reservations/{id}/location")
  public ResponseEntity<?> batchLocations(
      Authentication auth,
      @PathVariable String id,
      @jakarta.validation.Valid @org.springframework.web.bind.annotation.RequestBody
          com.example.oswc.tracking.dto.BatchLocationsRequest req) {
    String managerId = auth.getName();
    var resp = trackingService.batchLocations(id, managerId, req.getLocations());
    return ResponseEntity.ok(resp);
  }
}
