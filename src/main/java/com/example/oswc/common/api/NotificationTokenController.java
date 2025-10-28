package com.example.oswc.common.api;

import com.example.oswc.common.ApiResponse;
import com.example.oswc.common.NotificationTokenDocument;
import com.example.oswc.common.NotificationTokenRepository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationTokenController {

  private final NotificationTokenRepository repo;

  public NotificationTokenController(NotificationTokenRepository repo) {
    this.repo = repo;
  }

  /** POST /notifications/tokens - FCM 토큰 등록 */
  @PostMapping("/tokens")
  public ResponseEntity<?> registerToken(
      Authentication auth,
      @RequestParam @NotBlank String token,
      @RequestParam(required = false) String as) {
    String userId = auth.getName();
    var doc = repo.findByFcmToken(token).orElseGet(NotificationTokenDocument::new);
    doc.setFcmToken(token);
    if ("MANAGER".equalsIgnoreCase(as)) {
      doc.setManagerId(userId);
    } else {
      doc.setUserId(userId);
    }
    repo.save(doc);
    return ResponseEntity.ok(ApiResponse.ok(java.util.Map.of("registered", true)));
  }
}
