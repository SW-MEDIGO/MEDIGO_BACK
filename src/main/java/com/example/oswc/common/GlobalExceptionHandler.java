package com.example.oswc.common;

import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  private ResponseEntity<Map<String, Object>> body(HttpStatus status, String code, String message) {
    return ResponseEntity.status(status)
        .body(
            Map.of(
                "status",
                "error",
                "error",
                Map.of(
                    "code",
                    code,
                    "message",
                    message == null || message.isBlank() ? code : message)));
  }

  // ---------- 400 Bad Request 계열 ----------
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e) {
    String msg =
        e.getBindingResult().getAllErrors().stream()
            .findFirst()
            .map(x -> x.getDefaultMessage())
            .orElse("BAD_REQUEST");
    return body(HttpStatus.BAD_REQUEST, "BAD_REQUEST", msg);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, Object>> handleConstraint(ConstraintViolationException e) {
    return body(HttpStatus.BAD_REQUEST, "BAD_REQUEST", e.getMessage());
  }

  @ExceptionHandler({
    HttpMessageNotReadableException.class,
    MissingServletRequestParameterException.class,
    HttpMediaTypeNotSupportedException.class
  })
  public ResponseEntity<Map<String, Object>> handleReadable(Exception e) {
    return body(HttpStatus.BAD_REQUEST, "BAD_REQUEST", e.getMessage());
  }

  // ---------- 비즈니스/커스텀 코드 매핑 ----------
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArg(IllegalArgumentException e) {
    String code = e.getMessage() == null ? "ILLEGAL_ARGUMENT" : e.getMessage();

    // 필요한 코드만 선별적으로 상태 결정
    return switch (code) {
      case "INVALID_CREDENTIALS" -> body(HttpStatus.UNAUTHORIZED, code, null); // 401
      case "INVALID_REFRESH_TOKEN", "REUSED_REFRESH_TOKEN" ->
          body(HttpStatus.UNAUTHORIZED, code, null); // 401
      case "EMAIL_EXISTS", "USERNAME_EXISTS" -> body(HttpStatus.CONFLICT, code, null); // 409
      case "USER_NOT_FOUND" -> body(HttpStatus.NOT_FOUND, code, null); // 404
      case "EMPTY_FILE", "UNSUPPORTED_FILE_TYPE", "FILE_TOO_LARGE" ->
          body(HttpStatus.BAD_REQUEST, code, null); // 400
      default -> body(HttpStatus.BAD_REQUEST, code, null); // 기본 400
    };
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException e) {
    String message = e.getMessage();
    if (message == null) {
      return body(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.");
    }

    // 예약 관련 예외
    if (message.contains("예약을 찾을 수 없습니다")) {
      return body(HttpStatus.NOT_FOUND, "RESERVATION_NOT_FOUND", message);
    }
    if (message.contains("해당 예약 정보를 조회할 권한이 없습니다")) {
      return body(HttpStatus.FORBIDDEN, "FORBIDDEN_ACCESS", message);
    }
    if (message.contains("이미 서비스가 완료된 예약은 취소할 수 없습니다")) {
      return body(HttpStatus.CONFLICT, "CANCEL_NOT_ALLOWED", message);
    }
    if (message.contains("이미 서비스가 시작되었거나 완료된 예약은 취소할 수 없습니다")) {
      return body(HttpStatus.CONFLICT, "CANCEL_NOT_ALLOWED", message);
    }
    if (message.contains("해당 예약을 취소할 권한이 없습니다")) {
      return body(HttpStatus.FORBIDDEN, "FORBIDDEN_ACCESS", message);
    }
    if (message.contains("매니저 계정으로만 접근 가능합니다.")) {
      return body(HttpStatus.FORBIDDEN, "FORBIDDEN_ACCESS", message);
    }

    // 추적 관련
    if (message.contains("실시간 추적이 아직 활성화되지 않았습니다")) {
      return body(HttpStatus.NOT_FOUND, "TRACKING_NOT_ACTIVE", message);
    }
    if (message.contains("아직 동행 서비스가 시작되지 않았습니다.")) {
      return body(HttpStatus.CONFLICT, "SERVICE_NOT_STARTED", message);
    }

    // 리뷰 관련
    if (message.contains("완료된 예약에 대해서만 후기를 작성할 수 있습니다.")) {
      return body(HttpStatus.FORBIDDEN, "FORBIDDEN_ACTION", message);
    }

    // 기본 처리
    return body(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", message);
  }

  // ---------- 500 Internal Server Error ----------
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleEtc(Exception e) {
    return body(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", e.getMessage());
  }
}
