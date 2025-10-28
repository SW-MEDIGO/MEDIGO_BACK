package com.example.oswc.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationService {

  private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
  private final NotificationProperties props;

  private final NotificationTokenRepository tokenRepository;

  public NotificationService(
      NotificationProperties props, NotificationTokenRepository tokenRepository) {
    this.props = props;
    this.tokenRepository = tokenRepository;
  }

  public void sendToUser(String userId, String title, String message) {
    var tokens = tokenRepository.findByUserId(userId);
    if (!props.isEnabled() || props.getServerKey() == null || props.getServerKey().isBlank()) {
      log.info("[NOTIFY:LOG][USER:{}][TOKENS:{}] {} - {}", userId, tokens.size(), title, message);
      return;
    }
    // TODO: tokens 반복 호출 → FCM HTTP 전송
    log.info("[NOTIFY:FCM][USER:{}][TOKENS:{}] {} - {}", userId, tokens.size(), title, message);
  }

  public void sendToManager(String managerId, String title, String message) {
    var tokens = tokenRepository.findByManagerId(managerId);
    if (!props.isEnabled() || props.getServerKey() == null || props.getServerKey().isBlank()) {
      log.info(
          "[NOTIFY:LOG][MANAGER:{}][TOKENS:{}] {} - {}", managerId, tokens.size(), title, message);
      return;
    }
    // TODO: tokens 반복 호출 → FCM HTTP 전송
    log.info(
        "[NOTIFY:FCM][MANAGER:{}][TOKENS:{}] {} - {}", managerId, tokens.size(), title, message);
  }
}
