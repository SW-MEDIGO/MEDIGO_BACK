package com.example.oswc.reservation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends MongoRepository<ReservationDocument, String> {

  Page<ReservationDocument> findByUserId(String userId, Pageable pageable);

  Page<ReservationDocument> findByUserIdAndStatus(
      String userId, ReservationStatus status, Pageable pageable);
}
