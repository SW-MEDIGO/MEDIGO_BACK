package com.example.oswc.tracking;

import com.example.oswc.common.ApiResponse;
import com.example.oswc.common.NotificationService;
import com.example.oswc.reservation.ReservationDocument;
import com.example.oswc.reservation.ReservationRepository;
import com.example.oswc.tracking.dto.TrackingResponse;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class TrackingService {

  private final TrackingRepository repository;
  private final NotificationService notificationService;
  private final ReservationRepository reservationRepository;

  public TrackingService(
      TrackingRepository repository,
      NotificationService notificationService,
      ReservationRepository reservationRepository) {
    this.repository = repository;
    this.notificationService = notificationService;
    this.reservationRepository = reservationRepository;
  }

  public ApiResponse<?> getTracking(String reservationId, String requesterId) {
    var doc =
        repository
            .findByReservationId(reservationId)
            .orElseThrow(() -> new RuntimeException("해당 예약의 실시간 추적이 아직 활성화되지 않았습니다."));

    // NOTE: 권한 체크는 추후 확장(사용자/보호자/매니저 매핑) 시에 보강 가능

    var resp = new TrackingResponse();
    resp.setReservationId(doc.getReservationId());
    if (doc.getStatus() != null) {
      resp.setTrackingStatus(doc.getStatus().name());
    }
    resp.setLatitude(doc.getLastLatitude());
    resp.setLongitude(doc.getLastLongitude());
    if (doc.getLastUpdatedAt() != null) {
      resp.setUpdatedAt(DateTimeFormatter.ISO_INSTANT.format(doc.getLastUpdatedAt()));
    }
    // managerName 은 실제 조인 대신 임시로 null 처리 또는 추후 채움
    resp.setManagerName(null);

    return ApiResponse.ok(
        Map.of(
            "reservation_id", resp.getReservationId(),
            "tracking_status", resp.getTrackingStatus(),
            "last_known_location",
                doc.getLastLatitude() == null || doc.getLastLongitude() == null
                    ? null
                    : Map.of(
                        "latitude", resp.getLatitude(),
                        "longitude", resp.getLongitude(),
                        "updated_at", resp.getUpdatedAt()),
            "manager_name", resp.getManagerName()));
  }

  public ApiResponse<?> updateStatus(String reservationId, String managerId, String statusUpper) {
    var doc = repository.findByReservationId(reservationId).orElseGet(TrackingDocument::new);
    doc.setReservationId(reservationId);
    doc.setManagerId(managerId);
    doc.setStatus(TrackingStatus.valueOf(statusUpper));
    doc.setLastUpdatedAt(java.time.Instant.now());
    repository.save(doc);

    // 병원 도착 시 사용자에게 푸시 알림
    if ("ARRIVED_AT_HOSPITAL".equals(statusUpper)) {
      ReservationDocument res = reservationRepository.findById(reservationId).orElse(null);
      if (res != null && res.getUserId() != null) {
        notificationService.sendToUser(res.getUserId(), "병원 도착", "매니저가 병원에 도착했습니다.");
      }
    }

    return com.example.oswc.common.ApiResponse.ok(
        java.util.Map.of(
            "message",
            switch (statusUpper) {
              case "ARRIVED_AT_HOSPITAL" -> "동행 상태가 '병원 도착'으로 업데이트되었습니다.";
              case "SERVICE_STARTED" -> "동행 상태가 '시작'으로 업데이트되었습니다.";
              case "SERVICE_COMPLETED" -> "동행 상태가 '완료'로 업데이트되었습니다.";
              default -> "업데이트되었습니다.";
            }));
  }

  public ApiResponse<?> batchLocations(
      String reservationId,
      String managerId,
      java.util.List<com.example.oswc.tracking.dto.BatchLocationsRequest.LocationDto> locations) {
    var doc = repository.findByReservationId(reservationId).orElse(null);
    if (doc == null
        || doc.getStatus() == null
        || doc.getStatus() == TrackingStatus.SERVICE_COMPLETED) {
      throw new RuntimeException("아직 동행 서비스가 시작되지 않았습니다.");
    }
    // 마지막 위치만 갱신 (단순화)
    var last = locations.get(locations.size() - 1);
    doc.setManagerId(managerId);
    doc.setLastLatitude(last.getLatitude());
    doc.setLastLongitude(last.getLongitude());
    doc.setLastUpdatedAt(java.time.Instant.parse(last.getTimestamp()));
    repository.save(doc);
    return com.example.oswc.common.ApiResponse.ok(
        java.util.Map.of("message", "위치가 성공적으로 업데이트되었습니다."));
  }
}
