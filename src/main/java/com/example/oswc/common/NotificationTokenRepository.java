package com.example.oswc.common;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTokenRepository
    extends MongoRepository<NotificationTokenDocument, String> {
  Optional<NotificationTokenDocument> findByFcmToken(String fcmToken);

  List<NotificationTokenDocument> findByUserId(String userId);

  List<NotificationTokenDocument> findByManagerId(String managerId);
}
