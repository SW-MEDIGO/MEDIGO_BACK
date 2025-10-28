package com.example.oswc.reservation.api;

import com.example.oswc.common.ApiResponse;
import com.example.oswc.reservation.ReservationService;
import com.example.oswc.reservation.dto.CreateReservationRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
@Validated
public class ReservationController {

  private final ReservationService reservationService;

  public ReservationController(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  /** GET /reservations - 예약 목록 조회 */
  @GetMapping
  public ResponseEntity<?> getReservations(
      Authentication auth,
      @RequestParam(required = false, defaultValue = "UPCOMING")
          @Pattern(regexp = "(?i)UPCOMING|PAST", message = "INVALID_INPUT")
          String period,
      @RequestParam(required = false, defaultValue = "ALL")
          @Pattern(
              regexp = "(?i)ALL|PENDING|CONFIRMED|COMPLETED|CANCELED",
              message = "INVALID_INPUT")
          String status,
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int limit) {

    String userId = auth.getName();
    ApiResponse<?> response =
        reservationService.getReservations(userId, period, status, page, limit);
    return ResponseEntity.ok(response);
  }

  /** POST /reservations - 신규 예약 신청 */
  @PostMapping
  public ResponseEntity<?> createReservation(
      Authentication auth, @Valid @RequestBody CreateReservationRequest request) {

    String userId = auth.getName();
    ApiResponse<?> response = reservationService.createReservation(userId, request);
    return ResponseEntity.status(201).body(response);
  }

  /** GET /reservations/{id} - 예약 상세 조회 */
  @GetMapping("/{id}")
  public ResponseEntity<?> getReservationDetail(Authentication auth, @PathVariable String id) {

    String userId = auth.getName();
    ApiResponse<?> response = reservationService.getReservationDetail(id, userId);
    return ResponseEntity.ok(response);
  }

  /** PATCH /reservations/cancel/{id} - 예약 취소 */
  @PatchMapping("/cancel/{id}")
  public ResponseEntity<?> cancelReservation(Authentication auth, @PathVariable String id) {

    String userId = auth.getName();
    ApiResponse<?> response = reservationService.cancelReservation(id, userId);
    return ResponseEntity.ok(response);
  }

  /** GET /reservations/{id}/events - 예약 이벤트 조회 */
  @GetMapping("/{id}/events")
  public ResponseEntity<?> listReservationEvents(Authentication auth, @PathVariable String id) {

    String userId = auth.getName();
    ApiResponse<?> response = reservationService.listReservationEvents(id, userId);
    return ResponseEntity.ok(response);
  }
}
