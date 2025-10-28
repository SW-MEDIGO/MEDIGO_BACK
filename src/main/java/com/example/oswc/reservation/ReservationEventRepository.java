package com.example.oswc.reservation;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationEventRepository
    extends MongoRepository<ReservationEventDocument, String> {

  java.util.List<ReservationEventDocument> findByReservationIdOrderByOccurredAtDesc(
      String reservationId);
}
