package com.example.oswc.common;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notification_tokens")
public class NotificationTokenDocument {

  @Id private String id;

  @Indexed private String userId; // 사용자 토큰 (선택)
  @Indexed private String managerId; // 매니저 토큰 (선택)

  @Indexed(unique = true)
  private String fcmToken; // 디바이스 고유 토큰

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getManagerId() {
    return managerId;
  }

  public void setManagerId(String managerId) {
    this.managerId = managerId;
  }

  public String getFcmToken() {
    return fcmToken;
  }

  public void setFcmToken(String fcmToken) {
    this.fcmToken = fcmToken;
  }
}
