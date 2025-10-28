package com.example.oswc.review;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends MongoRepository<ReviewDocument, String> {
  Optional<ReviewDocument> findByReservationId(String reservationId);

  java.util.List<ReviewDocument> findByManagerId(String managerId);

  java.util.List<ReviewDocument> findByManagerIdOrderByCreatedAtDesc(String managerId);
}
