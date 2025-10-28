package com.example.oswc.manager;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerAvailabilityRepository
    extends MongoRepository<ManagerAvailabilityDocument, String> {
  Optional<ManagerAvailabilityDocument> findByManagerId(String managerId);
}
