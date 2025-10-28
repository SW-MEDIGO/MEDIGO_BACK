package com.example.oswc.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationService {

  private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

  public void sendToUser(String userId, String title, String message) {
    // 실제 푸시 연동(FCM 등) 전 스텁: 로그로 대체
    log.info("[NOTIFY][USER:{}] {} - {}", userId, title, message);
  }

  public void sendToManager(String managerId, String title, String message) {
    // 실제 푸시 연동(FCM 등) 전 스텁: 로그로 대체
    log.info("[NOTIFY][MANAGER:{}] {} - {}", managerId, title, message);
  }
}
