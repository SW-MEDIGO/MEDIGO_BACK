package com.example.oswc.reservation;

import com.example.oswc.common.ApiResponse;
import com.example.oswc.reservation.dto.CreateReservationRequest;
import com.example.oswc.reservation.dto.ReservationDetailResponse;
import com.example.oswc.reservation.dto.ReservationResponse;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

  private final ReservationRepository reservationRepository;

  public ReservationService(ReservationRepository reservationRepository) {
    this.reservationRepository = reservationRepository;
  }

  /** 예약 목록 조회 */
  public ApiResponse<?> getReservations(
      String userId, String period, String status, int page, int limit) {

    // period 유효성 및 기본값(UPCOMING)
    boolean isUpcoming;
    if (period == null || period.isBlank()) {
      isUpcoming = true;
    } else if ("UPCOMING".equalsIgnoreCase(period)) {
      isUpcoming = true;
    } else if ("PAST".equalsIgnoreCase(period)) {
      isUpcoming = false;
    } else {
      // 잘못된 period 값
      throw new IllegalArgumentException("INVALID_INPUT");
    }

    // 정렬: UPCOMING → 오름차순, PAST → 내림차순
    org.springframework.data.domain.Sort sort =
        isUpcoming
            ? org.springframework.data.domain.Sort.by("reservationDatetime").ascending()
            : org.springframework.data.domain.Sort.by("reservationDatetime").descending();

    Pageable pageable = PageRequest.of(Math.max(page - 1, 0), limit, sort);

    // now Instant (Document가 Instant를 사용)
    Instant nowInstant = Instant.now();

    boolean filterByStatus = status != null && !status.isBlank() && !"ALL".equalsIgnoreCase(status);
    Page<ReservationDocument> pageResult;
    if (isUpcoming) {
      if (filterByStatus) {
        ReservationStatus reservationStatus;
        try {
          reservationStatus = ReservationStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
          throw new IllegalArgumentException("INVALID_INPUT");
        }
        pageResult =
            reservationRepository.findByUserIdAndStatusAndReservationDatetimeGreaterThanEqual(
                userId, reservationStatus, nowInstant, pageable);
      } else {
        pageResult =
            reservationRepository.findByUserIdAndReservationDatetimeGreaterThanEqual(
                userId, nowInstant, pageable);
      }
    } else {
      if (filterByStatus) {
        ReservationStatus reservationStatus;
        try {
          reservationStatus = ReservationStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
          throw new IllegalArgumentException("INVALID_INPUT");
        }
        pageResult =
            reservationRepository.findByUserIdAndStatusAndReservationDatetimeLessThan(
                userId, reservationStatus, nowInstant, pageable);
      } else {
        pageResult =
            reservationRepository.findByUserIdAndReservationDatetimeLessThan(
                userId, nowInstant, pageable);
      }
    }

    List<ReservationDocument> reservations = pageResult.getContent();

    Map<String, Object> data = new HashMap<>();
    data.put(
        "pagination",
        Map.of(
            "currentPage", pageResult.getNumber() + 1,
            "totalPages", pageResult.getTotalPages(),
            "totalItems", pageResult.getTotalElements()));
    data.put(
        "reservations", reservations.stream().map(this::toResponse).collect(Collectors.toList()));

    return ApiResponse.ok(data);
  }

  /** 예약 생성 */
  public ApiResponse<?> createReservation(String userId, CreateReservationRequest request) {
    ReservationDocument reservation = new ReservationDocument();
    reservation.setUserId(userId);
    reservation.setHospitalName(request.getHospitalName());
    // ISO 8601 입력을 Instant로 파싱 (실패 시 400)
    try {
      Instant parsed = Instant.parse(request.getReservationDatetime());
      reservation.setReservationDatetime(parsed);
    } catch (Exception e) {
      throw new IllegalArgumentException("INVALID_INPUT");
    }
    reservation.setContent(request.getContent());
    reservation.setStatus(ReservationStatus.PENDING);
    reservation.setDesignatedManagerId(request.getDesignatedManagerId());

    // 성별 설정
    if (request.getManagerGender() != null && !request.getManagerGender().isEmpty()) {
      reservation.setManagerGender(ManagerGender.valueOf(request.getManagerGender().toUpperCase()));
    }

    reservation.setPaymentCardId(request.getPaymentCardId());
    reservation.setCreatedAtEpochMillis(Instant.now().toEpochMilli());
    reservation.setUpdatedAtEpochMillis(Instant.now().toEpochMilli());

    ReservationDocument saved = reservationRepository.save(reservation);

    // TODO: 매니저 매칭 로직 구현 (현재는 단순 저장만)

    Map<String, Object> data = new HashMap<>();
    data.put("reservation_id", saved.getId());
    data.put("message", "예약이 성공적으로 접수되었습니다.");

    return ApiResponse.ok(data);
  }

  /** 예약 상세 조회 */
  public ApiResponse<?> getReservationDetail(String id, String userId) {
    ReservationDocument reservation =
        reservationRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));

    // 권한 확인 (본인만 조회 가능)
    if (!reservation.getUserId().equals(userId)) {
      throw new RuntimeException("해당 예약 정보를 조회할 권한이 없습니다.");
    }

    ReservationDetailResponse response = new ReservationDetailResponse();
    response.setId(reservation.getId());
    response.setHospitalName(reservation.getHospitalName());
    if (reservation.getReservationDatetime() != null) {
      response.setReservationDatetime(
          DateTimeFormatter.ISO_INSTANT.format(reservation.getReservationDatetime()));
    }
    response.setContent(reservation.getContent());
    response.setStatus(reservation.getStatus());

    // 요청한 매니저 선호사항
    Map<String, String> preferences = new HashMap<>();
    if (reservation.getManagerGender() != null) {
      preferences.put("gender", reservation.getManagerGender().name());
    }
    response.setRequestedManagerPreferences(preferences);

    // 결제 정보 (임시)
    Map<String, String> paymentInfo = new HashMap<>();
    paymentInfo.put("card_brand", "KB국민카드");
    paymentInfo.put("card_last_four", "1234");
    response.setPaymentInfo(paymentInfo);

    // 사용자 정보 (임시)
    Map<String, Object> user = new HashMap<>();
    user.put("id", reservation.getUserId());
    user.put("name", "사용자명");
    response.setUser(user);

    // 매니저 정보
    if (reservation.getManagerId() != null) {
      Map<String, Object> manager = new HashMap<>();
      manager.put("id", reservation.getManagerId());
      manager.put("name", reservation.getManagerName());
      manager.put("profile_image_url", "https://...");
      manager.put("is_verified", true);
      response.setManager(manager);
    }

    return ApiResponse.ok(response);
  }

  /** 예약 취소 */
  public ApiResponse<?> cancelReservation(String id, String userId) {
    ReservationDocument reservation =
        reservationRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));

    // 권한 확인
    if (!reservation.getUserId().equals(userId)) {
      throw new RuntimeException("해당 예약을 취소할 권한이 없습니다.");
    }

    // 취소 가능 여부 확인
    if (reservation.getStatus() == ReservationStatus.COMPLETED) {
      throw new RuntimeException("이미 서비스가 완료된 예약은 취소할 수 없습니다.");
    }

    reservation.setStatus(ReservationStatus.CANCELED);
    reservation.setUpdatedAtEpochMillis(Instant.now().toEpochMilli());
    reservationRepository.save(reservation);

    Map<String, Object> data = new HashMap<>();
    data.put("message", "예약이 정상적으로 취소되었습니다.");

    return ApiResponse.ok(data);
  }

  /** ReservationDocument를 ReservationResponse로 변환 */
  private ReservationResponse toResponse(ReservationDocument doc) {
    ReservationResponse response = new ReservationResponse();
    response.setId(doc.getId());
    response.setHospitalName(doc.getHospitalName());
    // Instant -> ISO 8601 문자열
    if (doc.getReservationDatetime() != null) {
      response.setReservationDatetime(
          DateTimeFormatter.ISO_INSTANT.format(doc.getReservationDatetime()));
    }
    response.setStatus(doc.getStatus());
    response.setManagerName(doc.getManagerName());
    response.setManagerGender(doc.getManagerGender());
    return response;
  }
}
