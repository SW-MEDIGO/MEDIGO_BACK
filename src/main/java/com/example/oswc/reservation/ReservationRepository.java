package com.example.oswc.reservation;

import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends MongoRepository<ReservationDocument, String> {

  Page<ReservationDocument> findByUserId(String userId, Pageable pageable);

  Page<ReservationDocument> findByUserIdAndStatus(
      String userId, ReservationStatus status, Pageable pageable);

  // 기간 필터링 (Instant 비교)
  Page<ReservationDocument> findByUserIdAndReservationDatetimeGreaterThanEqual(
      String userId, Instant reservationDatetime, Pageable pageable);

  Page<ReservationDocument> findByUserIdAndReservationDatetimeLessThan(
      String userId, Instant reservationDatetime, Pageable pageable);

  Page<ReservationDocument> findByUserIdAndStatusAndReservationDatetimeGreaterThanEqual(
      String userId, ReservationStatus status, Instant reservationDatetime, Pageable pageable);

  Page<ReservationDocument> findByUserIdAndStatusAndReservationDatetimeLessThan(
      String userId, ReservationStatus status, Instant reservationDatetime, Pageable pageable);
}
