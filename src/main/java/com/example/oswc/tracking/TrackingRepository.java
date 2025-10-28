package com.example.oswc.tracking;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackingRepository extends MongoRepository<TrackingDocument, String> {
  Optional<TrackingDocument> findByReservationId(String reservationId);
}
