package com.example.oswc.manager.api;

import com.example.oswc.common.ApiResponse;
import com.example.oswc.manager.ManagerAvailabilityService;
import com.example.oswc.manager.dto.ManagerAvailabilityRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/managers/me")
public class ManagerAvailabilityController {

  private final ManagerAvailabilityService service;

  public ManagerAvailabilityController(ManagerAvailabilityService service) {
    this.service = service;
  }

  /** GET /managers/me/availability */
  @GetMapping("/availability")
  public ResponseEntity<?> getMine(Authentication auth) {
    String managerId = auth.getName();
    String role =
        (String) auth.getAuthorities().stream().findFirst().map(Object::toString).orElse("USER");
    ApiResponse<?> resp = service.getMine(managerId, role);
    return ResponseEntity.ok(resp);
  }

  /** PATCH /managers/me/availability */
  @PatchMapping("/availability")
  public ResponseEntity<?> patchMine(
      Authentication auth, @Valid @RequestBody ManagerAvailabilityRequest req) {
    String managerId = auth.getName();
    String role =
        (String) auth.getAuthorities().stream().findFirst().map(Object::toString).orElse("USER");
    ApiResponse<?> resp = service.patchMine(managerId, role, req);
    return ResponseEntity.ok(resp);
  }
}
